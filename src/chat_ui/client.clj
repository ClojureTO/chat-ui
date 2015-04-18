(ns chat-ui.client
  (:require [chat-client.core :as client]))

(def chat-client (atom nil))

(defn create [host port]
  (reset! chat-client (client/create host port)))

(defn send-message [message]
  (client/send-message @chat-client message))

(defn close []
  (client/close @chat-client))
