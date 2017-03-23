package com.wokkaflokka.avploopdemo;

import android.support.test.espresso.EspressoException;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.DataInteraction;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * This test demonstrates a condition in the Espresso framework that allows a {@link DataInteraction}
 * to enter an infinite loop that is interminable from within the Espresso. In such a state, the
 * test runs forever barring an action from an external agent. The details of the failure condition
 * are documented in detail in {@link HangingAdapterViewProtocol}.
 *
 * Created by ecoxe on 21/03/2017.
 */
@RunWith(AndroidJUnit4.class)
public class HangingAdapterViewProtocolTest {

    @Rule
    public ActivityTestRule<SimpleListActivity> bugRule = new ActivityTestRule<>(SimpleListActivity.class, true, false);

    @Rule
    public ActivityTestRule<EmptyListActivity> emptyRule = new ActivityTestRule<>(EmptyListActivity.class, true, false);

    @Rule
    public ActivityTestRule<DuplicateContentListActivity> duplicateRule = new ActivityTestRule<>(DuplicateContentListActivity.class, true, false);

    /**
     * Verifies that Espresso validly terminates on an empty ListView with a
     * malicious AdapterViewProtocol.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionFailsBeforeHangingOnEmptyAdapterView() {
        emptyRule.launchActivity(null);

        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }

    /**
     * Verifies that Espresso validly terminates on a ListView with a
     * malicious AdapterViewProtocol when the data matcher fails.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionFailsBeforeHangingWhenDataMatcherFails() {
        bugRule.launchActivity(null);

        onData(is("bad data"))
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }

    /**
     * Verifies that Espresso validly terminates on a ListView with a
     * malicious AdapterViewProtocol when the position matcher fails.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionFailsBeforeHangingWhenPositionMatcherFails() {
        bugRule.launchActivity(null);

        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .atPosition(1)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }

    /**
     * Verifies that Espresso validly terminates on a ListView with a
     * malicious AdapterViewProtocol when the data matcher matches multiple items.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionFailsBeforeHangingOnDuplicateContent() {
        duplicateRule.launchActivity(null);

        onData(is("oops"))
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }

    /**
     * Verifies that Espresso validly terminates on a ListView with a
     * malicious AdapterViewProtocol when the data matcher matches multiple items.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionFailsBeforeHangingOnAmbiguousPosition() {
        duplicateRule.launchActivity(null);

        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }

    /**
     * Instead of failing or passing, this test currently hangs forever (despite the view being
     * visibly present).
     *
     * My suggested fix at the moment would be to raise some implementation of {@link EspressoException} --
     * for instance, {@link PerformException} or some sensible alternative.
     *
     * This test is currently ignored so the valid tests in this suite can pass and terminate.
     */
    @Ignore
    @Test(expected = PerformException.class)
    public void testInteractionHangsForever() {
        bugRule.launchActivity(null);

        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }
}
