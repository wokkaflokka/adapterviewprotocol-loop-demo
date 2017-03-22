package com.wokkaflokka.avploopdemo;

import android.support.test.espresso.EspressoException;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.DataInteraction;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
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
    public ActivityTestRule<SimpleListActivity> rule = new ActivityTestRule<>(SimpleListActivity.class, true, true);

    /**
     * Instead of failing or passing, this test currently hangs forever (despite the view being
     * visibly present).
     *
     * My suggested fix at the moment would be to raise some implementation of {@link EspressoException} --
     * for instance, {@link PerformException} or some sensible alternative.
     */
    @Test(expected = PerformException.class)
    public void testDataInteractionHangsForever() {
        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .usingAdapterViewProtocol(HangingAdapterViewProtocol.INSTANCE)
                .atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());

        fail();
    }
}
