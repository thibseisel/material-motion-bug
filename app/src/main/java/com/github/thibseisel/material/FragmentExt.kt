package com.github.thibseisel.material

import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment

fun Fragment.startPostponedEnterTransitionWhenDrawn() {
    (requireView().parent as? ViewGroup)?.doOnPreDraw {
        startPostponedEnterTransition()
    }
}