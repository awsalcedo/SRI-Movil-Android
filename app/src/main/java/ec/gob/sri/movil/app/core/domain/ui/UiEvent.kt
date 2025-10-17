package ec.gob.sri.movil.app.core.domain.ui

/**
 * Base interface for UI events that trigger actions in ViewModels.
 * All feature-specific events should implement this interface.
 */
sealed interface UiEvent

/**
 * Common UI events that are used across multiple features
 */
sealed interface CommonUiEvent : UiEvent {
    
    /**
     * Event to dismiss error messages
     */
    data object DismissError : CommonUiEvent
    
    /**
     * Event to refresh/retry the current operation
     */
    data object Retry : CommonUiEvent
    
    /**
     * Event to navigate back
     */
    data object NavigateBack : CommonUiEvent
    
    /**
     * Event to clear all data and reset to initial state
     */
    data object ClearData : CommonUiEvent
}

/**
 * Extension functions for common event handling patterns
 */

/**
 * Checks if an event is a common UI event
 */
fun UiEvent.isCommonEvent(): Boolean = this is CommonUiEvent

/**
 * Checks if an event should trigger a refresh/retry operation
 */
fun UiEvent.isRetryEvent(): Boolean = this is CommonUiEvent.Retry

/**
 * Checks if an event should dismiss error states
 */
fun UiEvent.isDismissErrorEvent(): Boolean = this is CommonUiEvent.DismissError
