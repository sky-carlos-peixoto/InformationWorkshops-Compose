package com.sky.techtalks_compose.ui.navigation

import com.sky.techtalks_compose.ui.navigation.NavigationKeys.Arg.CATEGORY_ID

object NavigationKeys {

    object Arg {
        const val CATEGORY_ID = "CategoryName"
    }

    object Route {
        const val CATEGORIES_LIST = "categories_list"
        const val CATEGORY_DETAIL = "$CATEGORIES_LIST/{$CATEGORY_ID}"
    }

}