package com.belyabl9.langlearning.service.impl.importer;

/**
 * Allows to configure importing process.
 * 
 * Available options:
 * - Strategy for dealing with duplicates (replace, skip, roll back, stop but keep already imported)
 */
public class ImporterSettings {
    private final ActionOnDuplicate actionOnDuplicate;

    public ImporterSettings(ActionOnDuplicate actionOnDuplicate) {
        this.actionOnDuplicate = actionOnDuplicate;
    }

    public ActionOnDuplicate getActionOnDuplicate() {
        return actionOnDuplicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImporterSettings that = (ImporterSettings) o;

        return actionOnDuplicate == that.actionOnDuplicate;
    }

    @Override
    public int hashCode() {
        return actionOnDuplicate != null ? actionOnDuplicate.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ImporterSettings{" +
                "actionOnDuplicate=" + actionOnDuplicate +
                '}';
    }
}
