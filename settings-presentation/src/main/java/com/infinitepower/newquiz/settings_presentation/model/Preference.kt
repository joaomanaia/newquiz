package com.infinitepower.newquiz.settings_presentation.model

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.infinitepower.newquiz.core.datastore.PreferenceRequest

/**
 * The basic building block that represents an individual setting displayed to a user in the preference hierarchy.
 */
sealed class Preference {
    abstract val title: String
    abstract val enabled: Boolean
    abstract val visible: Boolean

    /**
     * A single [Preference] item
     */
    sealed class PreferenceItem<T> : Preference() {
        abstract val summary: String?
        abstract val singleLineTitle: Boolean

        /**
         * Represents the keys of a SwitchPreference that controls the state of this Preference.
         * When the corresponding switch is turned off, this Preference is disabled and is unable to be modified.
         */
        abstract val dependency: List<PreferenceRequest<Boolean>>

        abstract val icon: @Composable (() -> Unit)?

        data class NavigationButton(
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val screenKey: ScreenKey,
            val iconImageVector: ImageVector,
            val itemSelected: Boolean,
            val screenExpanded: Boolean,
            val inMainPage: Boolean,
            val onClick: () -> Unit
        ) : PreferenceItem<String>()

        /**
         * 	A basic [PreferenceItem] that only displays text.
         */
        data class TextPreference(
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val onClick: () -> Unit = {}
        ) : PreferenceItem<String>()

        /**
         * 	A [PreferenceItem] that provides a two-state toggleable option.
         *
         * 	@param onCheckChange Called when the switch is toggled, the value is the new state of the switch
         * 	or the value of the dependency if the switch is disabled
         * 	@param primarySwitch If true, the switch will be displayed with the primary color in the container
         * 	and the container will have a tonal elevation and padding.
         */
        data class SwitchPreference(
            val request: PreferenceRequest<Boolean>,
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val onCheckChange: (newValue: Boolean) -> Unit = {},
            val primarySwitch: Boolean = false
        ) : PreferenceItem<Boolean>()

        /**
         * 	A [PreferenceItem] that displays a list of entries as a dialog.
         * 	Only one entry can be selected at any given time.
         */
        data class ListPreference(
            val request: PreferenceRequest<String>,
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val entries: Map<String, String>,
            val onItemClick: (value: String) -> Unit = {}
        ) : PreferenceItem<String>()

        /**
         * A [PreferenceItem] that displays a list of entries as a dialog.
         * Multiple entries can be selected at the same time.
         */
        data class MultiSelectListPreference(
            val request: PreferenceRequest<Set<String>>,
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val entries: Map<String, String>,
        ) : PreferenceItem<Set<String>>()

        /**
         * A [PreferenceItem] that displays a seekBar and the currently selected value.
         */
        data class SeekBarPreference(
            val request: PreferenceRequest<Int>,
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val valueRange: ClosedRange<Int> = 0..10,
            val steps: Int = 0,
            val valueRepresentation: (Int) -> String = { it.toString() }
        ) : PreferenceItem<Int>()

        /**
         * 	A [PreferenceItem] that displays a list of entries as a DropDownMenu.
         * 	Only one entry can be selected at any given time.
         */
        data class DropDownMenuPreference(
            val request: PreferenceRequest<String>,
            override val title: String,
            override val summary: String? = null,
            override val singleLineTitle: Boolean = true,
            override val dependency: List<PreferenceRequest<Boolean>> = emptyList(),
            override val icon: @Composable (() -> Unit)? = null,
            override val enabled: Boolean = true,
            override val visible: Boolean = true,

            val entries: Map<String, String>,
        ) : PreferenceItem<String>()
    }

    /**
     * A container for multiple [PreferenceItem]s
     */
    data class PreferenceGroup(
        override val title: String,
        override val enabled: Boolean = true,
        override val visible: Boolean = true,

        val preferenceItems: List<PreferenceItem<out Any>>
    ) : Preference()

    /**
     * @param title in custom preference title will be the semantic description
     */
    data class CustomPreference(
        override val title: String,
        override val enabled: Boolean = true,
        override val visible: Boolean = true,

        val content: @Composable BoxScope.() -> Unit
    ) : Preference()
}