package xxl;

import xxl.content.Result;

/** Interface that represents a subject. */
public interface Subject {

    void attach(Observer observer);
    void detach(Observer observer);
    void notifyIfChanged(Result<?> result);
}