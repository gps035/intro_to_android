package com.example.naughtsandcrosses


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTests {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    private fun clickTile(id: Int) = onView(allOf(withId(id), isDisplayed())).apply { perform(click()) }

    @Test
    fun canWinDiagonally() {
        clickTile(R.id.row1Col1)
        clickTile(R.id.row1Col2)
        clickTile(R.id.row1Col3)
        clickTile(R.id.row2Col1)
        clickTile(R.id.row2Col2)
        clickTile(R.id.row2Col3)
        clickTile(R.id.row3Col1)

        val button = onView(withId(R.id.restart))
        button.check(matches(isDisplayed()))
        button.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        val textView = onView(withId(R.id.message))
        textView.check(matches(isDisplayed()))
        textView.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        textView.check(matches(withText("O's win.\nClick restart to play again")))
    }

    @Test
    fun canDraw() {
        clickTile(R.id.row1Col1)
        clickTile(R.id.row1Col2)
        clickTile(R.id.row1Col3)
        clickTile(R.id.row2Col2)
        clickTile(R.id.row2Col1)
        clickTile(R.id.row2Col3)
        clickTile(R.id.row3Col2)
        clickTile(R.id.row3Col1)
        clickTile(R.id.row3Col3)

        val button = onView(withId(R.id.restart))
        button.check(matches(isDisplayed()))
        button.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        val textView = onView(withId(R.id.message))
        textView.check(matches(isDisplayed()))
        textView.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        textView.check(matches(withText("Draw.\nClick restart to play again")))
    }
}
