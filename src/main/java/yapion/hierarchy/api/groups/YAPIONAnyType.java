/*
 * Copyright 2019,2020,2021 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yapion.hierarchy.api.groups;

import lombok.NonNull;
import yapion.annotations.api.InternalAPI;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.api.*;
import yapion.hierarchy.api.storage.Comments;
import yapion.hierarchy.output.AbstractOutput;
import yapion.hierarchy.output.StringOutput;
import yapion.hierarchy.output.flavours.CheckingFlavour;
import yapion.hierarchy.output.flavours.Flavour;
import yapion.hierarchy.types.YAPIONElementPath;
import yapion.parser.YAPIONParser;
import yapion.path.YAPIONPath;
import yapion.path.YAPIONPathParser;
import yapion.utils.ReferenceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@InternalAPI
public abstract class YAPIONAnyType implements ObjectSearch, ObjectPath, ObjectType, ObjectOutput, Comments, TypeConversion {

    // Reference Value System
    private AtomicReference<Long> referenceValue = new AtomicReference<>(null);
    private ReferenceFunction referenceFunction = null;

    // Parent System
    private YAPIONAnyType parent = null;
    private boolean valuePresent = false;

    // Comment System
    private List<String> comments = new ArrayList<>();

    protected final void cacheReferenceValue(long referenceValue) {
        this.referenceValue.set(referenceValue);
    }

    protected final long getReferenceValue() {
        return referenceValue.get();
    }

    protected final void discardReferenceValue() {
        referenceValue.set(null);
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            yapionAnyType.referenceValue.set(null);
        }
    }

    protected final boolean hasReferenceValue() {
        return referenceValue.get() != null;
    }

    @Override
    public long referenceValue(@NonNull ReferenceFunction referenceFunction) {
        if (hasReferenceValue() && this.referenceFunction == referenceFunction) {
            return getReferenceValue();
        }
        this.referenceFunction = referenceFunction;
        cacheReferenceValue(referenceValueProvider(referenceFunction) & 0x7FFFFFFFFFFFFFFFL);
        return getReferenceValue();
    }

    protected long referenceValueProvider(ReferenceFunction referenceFunction) {
        throw new YAPIONException("No Reference Value could be calculated");
    }

    public YAPIONAnyType internalCopy() {
        YAPIONAnyType yapionAnyType = YAPIONParser.parse("{" + toYAPION(new StringOutput()).getResult() + "}").getAnyType("");
        yapionAnyType.removeParent();
        return yapionAnyType;
    }

    // Depth System / Pretty YAPION String
    public final int getDepth() {
        int depth = 0;
        YAPIONAnyType yapionAnyType = this;
        while ((yapionAnyType = yapionAnyType.getParent()) != null) {
            depth++;
        }
        return depth;
    }

    // Path System
    @Override
    public final YAPIONElementPath getPath() {
        return new YAPIONElementPath(this);
    }

    @Override
    public String getPath(YAPIONAnyType yapionAnyType) {
        return "";
    }

    public final void setParent(YAPIONAnyType yapionAnyType) {
        if (valuePresent) {
            return;
        }
        this.parent = yapionAnyType;
        valuePresent = true;
    }

    public final void removeParent() {
        parent = null;
        valuePresent = false;
    }

    public final YAPIONAnyType getParent() {
        return parent;
    }

    public final boolean hasParent() {
        return valuePresent;
    }

    protected final boolean isValuePresent() {
        return valuePresent;
    }

    // Traverse System
    public final Optional<YAPIONSearchResult<?>> getViaPath(String... s) {
        if (s == null || s.length == 0) {
            return Optional.of(new YAPIONSearchResult<>(this));
        }
        Optional<YAPIONSearchResult<?>> optional = Optional.of(new YAPIONSearchResult<>(this));
        for (String value : s) {
            if (value == null) return Optional.empty();
            if (!optional.isPresent()) return Optional.empty();
            optional = optional.get().value.getViaPath(value);
        }
        return optional;
    }

    @Override
    public Optional<YAPIONSearchResult<?>> getViaPath(String key) {
        return Optional.empty();
    }

    protected <T extends AbstractOutput> void outputComments(T abstractOutput, Flavour flavour, Flavour.PrettifyBehaviour prettifyBehaviour, List<String> comments, String indent) {
        comments.forEach(s -> {
            String[] strings = s.split("\n|\r\n|\n\r|\r");
            if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                abstractOutput.consumePrettified(indent);
            } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                abstractOutput.consume(indent);
            }
            flavour.begin(Flavour.HierarchyTypes.COMMENT, abstractOutput);
            for (int i = 0; i < strings.length; i++) {
                if (i != 0) {
                    if (prettifyBehaviour == Flavour.PrettifyBehaviour.CHOOSEABLE) {
                        abstractOutput.consumePrettified(indent);
                    } else if (prettifyBehaviour == Flavour.PrettifyBehaviour.ALWAYS) {
                        abstractOutput.consume(indent);
                    }
                }

                String current = strings[i];
                int index = 0;
                while (index < current.length() && Character.isWhitespace(current.charAt(index))) {
                    index++;
                }
                flavour.elementValue(Flavour.HierarchyTypes.COMMENT, abstractOutput, new Flavour.ValueData<>(null, current.substring(index)));
            }
            flavour.end(Flavour.HierarchyTypes.COMMENT, abstractOutput);
        });
    }

    protected <T extends AbstractOutput> void outputComments(T abstractOutput, List<String> comments, String indent) {
        comments.forEach(s -> {
            String[] strings = s.split("\n|\r\n|\n\r|\r");
            abstractOutput.consumePrettified(indent);
            abstractOutput.consume("/*");
            for (int i = 0; i < strings.length; i++) {
                if (i != 0) {
                    abstractOutput.consume(indent);
                }

                String current = strings[i];
                int index = 0;
                while (index < current.length() && Character.isWhitespace(current.charAt(index))) {
                    index++;
                }
                abstractOutput.consume(current.substring(index));
            }
            abstractOutput.consume("*/");
        });
    }

    protected final CheckingFlavour convert(Flavour flavour) {
        if (flavour instanceof CheckingFlavour) {
            return (CheckingFlavour) flavour;
        }
        return new CheckingFlavour(flavour);
    }

    public List<String> getComments() {
        return comments;
    }

    public List<YAPIONAnyType> select(YAPIONPath yapionPath) {
        return yapionPath.apply(this);
    }

    public List<YAPIONAnyType> select(String pathIdentifier) {
        return select(new YAPIONPathParser(pathIdentifier).parse());
    }
}
