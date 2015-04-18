(ns chat-ui.routes.home
  (:require [chat-ui.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]
            [chat-ui.client :as client]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/login" [user]
        (client/send-message (str "USER " user))
        (response {:result :ok}))
  (GET "/users" []
       (client/send-message "LIST")
       ;;we have to provide a way to associate
       ;;a request from the client with the response
       ;;from the server

       ;;currently chat-server doesn't provide any
       ;;unique identifier to correlate the request with
       ;;a specific response
       (response
        {:result "todo"}))
  (POST "/message" [message]
        (println message)
        (response
         {:result    :ok
          :timestamp (java.util.Date.)}))
  (GET "/about" [] (about-page)))
