package yapion.serializing;

import yapion.annotations.*;
import yapion.exceptions.YAPIONException;

public class StateManager {

    private String state;
    private boolean emptyState = false;

    public StateManager(String state) {
        if (state.contains(" ")) {
            throw new YAPIONException("State cannot contain ' ': \"" + state + "\"");
        }
        this.state = state;
        this.emptyState = state.isEmpty();
    }

    public boolean is(YAPIONLoad yapionLoad) {
        if (emptyState) return true;
        return yapionLoad.context().contains(state);
    }

    public boolean is(YAPIONLoadExclude yapionLoadExclude) {
        if (emptyState) return true;
        return yapionLoadExclude.context().contains(state);
    }

    public boolean is(YAPIONOptimize yapionOptimize) {
        if (emptyState) return true;
        return yapionOptimize.context().contains(state);
    }

    public boolean is(YAPIONSave yapionSave) {
        if (emptyState) return true;
        return yapionSave.context().contains(state);
    }

    public boolean is(YAPIONSaveExclude yapionSaveExclude) {
        if (emptyState) return true;
        return yapionSaveExclude.context().contains(state);
    }

}
