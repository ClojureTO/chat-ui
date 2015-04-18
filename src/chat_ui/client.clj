(ns chat-ui.client
  (:require [chat-client.core :as client]))

(def chat-client (atom nil))
(def messages (atom []))

(defn create [host port]
  (reset! chat-client (client/create host port)))

(defn send-message [message]
  (client/send-message @chat-client message))

(defn read-message [message]
  (swap! messages conj message))

(defn start-message-reader []
  (client/start-reader-thread @chat-client read-message))

(defn close []
  (client/close @chat-client))
