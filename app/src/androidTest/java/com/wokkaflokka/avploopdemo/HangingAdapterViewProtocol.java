package com.wokkaflokka.avploopdemo;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.AdapterDataLoaderAction;
import android.support.test.espresso.action.AdapterViewProtocol;
import android.support.test.espresso.action.AdapterViewProtocols;
import android.support.test.espresso.core.deps.guava.base.Optional;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * An {@link AdapterViewProtocol} instance with default behavior, with the exception of overriding
 * {@link AdapterViewProtocol#isDataRenderedWithinAdapterView(AdapterView, AdaptedData)}.
 *
 * Every {@link DataInteraction} requires an instance of an AdapterViewProtocol. Whenever a
 * {@link ViewInteraction} is taken on a DataInteraction, the data from the {@link AdapterView}
 * is loaded using the {@link AdapterDataLoaderAction}. When the AdapterDataLoaderAction is
 * instantiated, the DataInteraction internally performs a ViewInteraction which invokes
 * {@link AdapterDataLoaderAction#perform(UiController, View)}.
 *
 * Observing this implementation of perform, one notes at line 136 that before exiting, the action
 * enters a loop that queries {@link AdapterViewProtocol#isDataRenderedWithinAdapterView(AdapterView, AdaptedData)}
 * for it's terminal condition. At first glance, there doesn't appear to be any mechanism internal
 * to the loop to break or terminate after a certain condition. Looking further, neither
 * {@link DataInteraction} or {@link ViewInteraction#doPerform(ViewAction)} seem to impose
 * an external failure condition on this action. With this information, it seems to be
 * the case that this method can enter into an infinite loop if the AdapterViewProtocol never
 * returns "true" when queried.
 *
 * Of course, in the general case this is a non-issue, and seems pedantic. In fact, there are real
 * conditions an {@link AdapterView} may enter this state. In fact, the {@link AdapterViewProtocols.StandardAdapterViewProtocol}
 * has comments hinting at the nature of this failure condition:
 *
 *     "// Occassionally we'll have to fight with smooth scrolling logic on our definition of when
 *      // there is extra scrolling to be done. In particular if the element is the first or last
 *      // element of the list, the smooth scroller may decide that no work needs to be done to scroll
 *      // to the element if a certain percentage of it is on screen. Ugh. Sigh. Yuck."
 *
 * The code then proceeds to institute a 90% visibility constraint on the provided view matcher.
 *
 * One can therefore imagine a case where a view does not satisfy this constraint -- for example,
 * each child view of the adapter may exceed the size of the viewport -- and if for any reason
 * scrolling does not bring the element into view, this condition will be triggered. Again, noting
 * the comment above, it is clear there are cases where an {@link AdapterView} may think contrary
 * to Espresso that scrolling is not required.
 *
 * One might further imagine that this may be reproducible in it's current form by experimenting using
 * child views that exceed the size viewport. While this was initially discovered under such
 * conditions, no thorough investigation has been given to that end.
 *
 * See:
 *    {@link DataInteraction#perform(ViewAction...)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.1_r1/com/google/android/apps/common/testing/ui/espresso/DataInteraction.java#128
 *    {@link DataInteraction#check(ViewAssertion)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.1_r1/com/google/android/apps/common/testing/ui/espresso/DataInteraction.java#141
 *    {@link DataInteraction#load()}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.1_r1/com/google/android/apps/common/testing/ui/espresso/DataInteraction.java#148
 *    {@link AdapterDataLoaderAction#perform(UiController, View)}
 *           https://android.googlesource.com/platform/frameworks/testing/+/android-support-test/espresso/core/src/main/java/android/support/test/espresso/action/AdapterDataLoaderAction.java#76
 *    {@link AdapterDataLoaderAction#perform(UiController, View)}, line 136
 *           https://android.googlesource.com/platform/frameworks/testing/+/android-support-test/espresso/core/src/main/java/android/support/test/espresso/action/AdapterDataLoaderAction.java#136
 *    {@link ViewInteraction#doPerform(ViewAction)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.1_r1/com/google/android/apps/common/testing/ui/espresso/ViewInteraction.java#102
 *    {@link AdapterViewProtocols.StandardAdapterViewProtocol#isDataRenderedWithinAdapterView(AdapterView, AdaptedData)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.0_r1/com/google/android/apps/common/testing/ui/espresso/action/AdapterViewProtocols.java#128
 *    {@link AdapterViewProtocols.StandardAdapterViewProtocol#isElementFullyRendered(AdapterView, int)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.0_r1/com/google/android/apps/common/testing/ui/espresso/action/AdapterViewProtocols.java#147
 *    {@link AdapterViewProtocols.StandardAdapterViewProtocol#makeDataRenderedWithinAdapterView(AdapterView, AdaptedData)}
 *           http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/5.0.0_r1/com/google/android/apps/common/testing/ui/espresso/action/AdapterViewProtocols.java#93
 *
 * Created by ecoxe on 21/03/2017.
 */
final class HangingAdapterViewProtocol implements AdapterViewProtocol {

    private HangingAdapterViewProtocol() { }

    static final HangingAdapterViewProtocol INSTANCE = new HangingAdapterViewProtocol();

    private static final AdapterViewProtocol DELEGATE = AdapterViewProtocols.standardProtocol();

    @Override
    public Iterable<AdaptedData> getDataInAdapterView(AdapterView<? extends Adapter> adapterView) {
        return DELEGATE.getDataInAdapterView(adapterView);
    }

    @Override
    public Optional<AdaptedData> getDataRenderedByView(AdapterView<? extends Adapter> adapterView, View descendantView) {
        return DELEGATE.getDataRenderedByView(adapterView, descendantView);
    }

    @Override
    public void makeDataRenderedWithinAdapterView(AdapterView<? extends Adapter> adapterView, AdaptedData data) {
        DELEGATE.makeDataRenderedWithinAdapterView(adapterView, data);
    }

    /**
     * Overriding this API to demonstrate the non-terminal loop condition in
     * {@link AdapterDataLoaderAction#perform(UiController, View)}, line 136.
     *
     * @return false
     */
    @Override
    public boolean isDataRenderedWithinAdapterView(AdapterView<? extends Adapter> adapterView, AdaptedData adaptedData) {
        return false;
    }
}
