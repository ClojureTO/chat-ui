(ns chat-ui.app
  (:require [chat-ui.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
