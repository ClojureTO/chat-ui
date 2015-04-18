(ns chat-ui.client
  (:require
   [goog.Timer :as timer]
   [goog.events :as events]))

(defn poll [poll-fn timeout]
  (let [timer (goog.Timer. timeout)]
    (do (poll-fn)
        (.start timer)
        (events/listen timer goog.Timer/TICK poll-fn))))
