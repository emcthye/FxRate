package io.github.emcthye.fxrate.currencyList;

import androidx.core.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.github.emcthye.fxrate.R;
import io.github.emcthye.fxrate.currencyTimeline.CurrencyTimelineActivity;
import io.github.emcthye.fxrate.entity.CurrencyEntity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class CurrencyListActivityTest {

    @Rule
    public ActivityScenarioRule<CurrencyListActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CurrencyListActivity.class);

    @Test
    public void render() {

        CurrencyEntity obj = new CurrencyEntity();
        List<Pair<String,String>> rate = new ArrayList<>();
        rate.add(new Pair<>("USD", "1-0.25"));
        obj.rates = rate;

        activityScenarioRule.getScenario().onActivity(activity -> activity.render(obj));

        onView(withId(R.id.rvCurrencyList)).perform(
                RecyclerViewActions.scrollTo(hasDescendant(
                        withText(containsString("USD")))));

        onView(withId(R.id.rvCurrencyList)).check(matches(hasChildCount(1)));

        Intents.init();
        onView(withId(R.id.rvCurrencyList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(hasComponent(CurrencyTimelineActivity.class.getName())));
        Intents.release();
    }

    @Test
    public void onResume() {
        activityScenarioRule.getScenario().moveToState(Lifecycle.State.RESUMED);
        activityScenarioRule.getScenario().onActivity(activity ->
                verify(activity.presenter).start());
    }

    @Test
    public void renderLoading() {

        activityScenarioRule.getScenario().onActivity(CurrencyListActivity::renderLoading);

        onView(withId(R.id.rlEmptyView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rvCurrencyList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rlLoadingView)).check(matches(isDisplayed()));
        onView(withId(R.id.rlErrorView)).check(matches(not(isDisplayed())));
    }

    @Test
    public void renderEmpty() {

        activityScenarioRule.getScenario().onActivity(CurrencyListActivity::renderEmpty);

        onView(withId(R.id.rlEmptyView)).check(matches(isDisplayed()));
        onView(withId(R.id.rvCurrencyList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rlLoadingView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rlErrorView)).check(matches(not(isDisplayed())));
    }

    @Test
    public void renderError() {

        activityScenarioRule.getScenario().onActivity(CurrencyListActivity::renderError);

        onView(withId(R.id.rlErrorView)).check(matches(isDisplayed()));
        onView(withId(R.id.rvCurrencyList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rlLoadingView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rlEmptyView)).check(matches(not(isDisplayed())));
    }

    @Test
    public void errorRetryClicked() {

        activityScenarioRule.getScenario().onActivity(CurrencyListActivity::renderError);
        onView(withId(R.id.btnErrorRetry)).perform(click());

        activityScenarioRule.getScenario().onActivity(activity ->
                verify(activity.presenter).refreshList());
    }

    @Test
    public void setLastUpdate() {
        activityScenarioRule.getScenario().onActivity(activity -> activity.setLastUpdate("33s ago"));
        onView(withId(R.id.tvLastUpdated)).check(matches(withText("Last Updated " + "33s ago")));
    }
}