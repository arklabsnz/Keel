Change Log
==========

Version 1.2.5
----------------------------

 * Adds `withState` convenience method for accessing state within the viewmodel

Version 1.1.5
----------------------------

 * Adds LiveData extension function `observeNonNull`
 * Handles error when state reduction fails
 * Updates dependencies

Version 1.1.2
----------------------------

  * Changes events subject to ReplaySubject in order to all buffer events it receives before it subscribes

Version 1.1.1
----------------------------

  * Changes events subject to BehaviorSubject in order to buffer events it receives before it subscribes

Version 1.0.1
----------------------------

  * Exposes events observable to allow subscribing to viewmodel events

Version 1.0.0
----------------------------

  * Initial release