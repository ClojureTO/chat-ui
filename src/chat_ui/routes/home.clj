(ns chat-ui.routes.home
  (:require [chat-ui.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
           (GET "/" [] (home-page))
           (POST "/message" [message]
                 (println message)
                 (response
                   {:result    :ok
                    :timestamp (java.util.Date.)}))
           (GET "/about" [] (about-page)))
