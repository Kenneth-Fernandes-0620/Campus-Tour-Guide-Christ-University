package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Pair;
import android.widget.Toast;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Pronunciation {

    private TextToSpeech textToSpeech;
    private int languageResult;
    private boolean noError;
    private final String errorMessage="Something went wrong with your text to speech engine";
    private PublishSubject<Boolean> engineIsReady=PublishSubject.create();
    private PublishSubject<Pair<String,Integer>> speakObservable=PublishSubject.create();
    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    public Pronunciation(Context context) {
        textToSpeech=new TextToSpeech(context, status -> {

            if (status!=TextToSpeech.ERROR){
                languageResult=  textToSpeech.setLanguage(Locale.ENGLISH);
                engineIsReady.onNext(true);
            } else {
                Toast.makeText(context,errorMessage
                        ,Toast.LENGTH_LONG).show();
            }

        });

        if (languageResult==TextToSpeech.LANG_MISSING_DATA||languageResult== TextToSpeech.LANG_NOT_SUPPORTED){
            noError =false;
            Toast.makeText(context,errorMessage
                    ,Toast.LENGTH_LONG).show();
        }else { noError =true;}


        compositeDisposable.add( Observable.combineLatest(speakObservable, engineIsReady,
                        (stringIntegerPair, aBoolean) -> stringIntegerPair)
                .subscribeOn(Schedulers.io())
                .subscribe(pair->{

                    if (noError)
                        textToSpeech.speak( (pair).first
                                ,(pair).second,null,null);
                }));


    }


    public void speak(String text,int queueMode){

        speakObservable.onNext(new Pair<>(text,queueMode));

    }

    public void stop(){
        if (textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        compositeDisposable.clear();
    }
}