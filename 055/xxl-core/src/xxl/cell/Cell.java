package xxl.cell;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

import xxl.content.Content;
import xxl.content.Result;
import xxl.Subject;
import xxl.Observer;

/** Class representing a cell. Cells can have content. */
public class Cell implements Subject, Observer, Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Content _content;
    private List<Observer> _observers = new ArrayList<>();
    private List<Subject>  _subjects  = new ArrayList<>();

    public Cell(Content content) {
        _content = content;
        startObservation();
    }

    public void setContent(Content content) {

        Result<?> currentResult = _content != null ? _content.getCurrentValue() : null;
        _content = content;
        notifyIfChanged(currentResult); // notify the observers if the evaluated content result changed 
        stopObservation();
        startObservation();
    }

    public Content getContent() {
        return _content;
    }

    public void attach(Observer observer) { _observers.add(observer); }

    public void detach(Observer observer) { 
        int i = _observers.indexOf(observer);
        if (i >= 0) { _observers.remove(observer); }
    }

    public void notifyIfChanged(Result<?> currentResult) {

        if (_content != null) {

            Result<?> evaluatedResult = _content.evaluate();

            // if the updated evaluation is equal to the previous evaluation
            if (evaluatedResult.equals(currentResult))          
                return;    // there is no need to notify the observers
            else
                _content.setCurrentValue(evaluatedResult);
        }
        for (Observer observer: _observers) {
            observer.update();
        }
    }

    public void startObservation() {
        
        if (_content == null)
            return;

        // receives the list of subjects to observe
        _subjects = _content.getDependency();

        for (Subject subject: _subjects) {
            subject.attach(this);
        }
    }

    public void stopObservation() {      
        
        for (Subject subject: _subjects) {
            subject.detach(this);
        }
        _subjects = new ArrayList<>();  // clear the list of subjects
    }

    public void update() {
        if (_content == null)
            return;
        Result<?> currentResult = _content.getCurrentValue();
        notifyIfChanged(currentResult);
    }
}