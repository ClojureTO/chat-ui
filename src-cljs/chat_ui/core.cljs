(ns chat-ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [reagent.session :as session]
            [reagent-forms.core :refer [bind-fields]]
            [ajax.core :refer [GET POST]]
            [chat-ui.client :refer [poll]])
  (:require-macros [secretary.core :refer [defroute]]))

(defn navbar []
  [:div.navbar.navbar-inverse.navbar-fixed-top
   [:div.container
    [:div.navbar-header
     [:a.navbar-brand {:href "#/"} "chat-ui"]]
    [:div.navbar-collapse.collapse
     [:ul.nav.navbar-nav
      [:li {:class (when (= :home (session/get :page)) "active")}
       [:a {:on-click #(secretary/dispatch! "#/")} "Home"]]
      [:li {:class (when (= :about (session/get :page)) "active")}
       [:a {:on-click #(secretary/dispatch! "#/about")} "About"]]]]]])

(defn about-page []
  [:div "this is the story of chat-ui... work in progress"])

(def user (atom nil))
(def messages (atom []))

(defn side-bar []
  (let [users (atom ["Bob" "Jane" "Alice"])
        selected-user (atom nil)]
    (fn []
      [:ul.list-group
       (into
         [:ul]
         (map
           (fn [user]
             [:li.list-group-item
              {:class    (if (= user @selected-user) "selected" "unselected")
               :on-click #(reset! selected-user user)}
              user])
           @users))])))

(defn message-list [messages]
  [:div
   (for [message messages]
     ^{:key message}
     [:p message])])

(defn login! [user]
  (POST "/login"
        {:params {:user @user}}))

(defn send-message! [message error]
  (POST "/message"
        {:params {:message @message}
         :handler #(do
                    (swap! messages conj @message)
                    (reset! message nil))
         :error-handler #(reset! error (:response %))}))

(defn value-of [event]
  (-> event .-target .-value))

(defn input-field [container]
  [:input.form-control
   {:type :text
    :value @container
    :on-change #(reset! container (value-of %))}])

(defn message-component []
  (let [message (atom nil)
        error (atom nil)]
    (fn []
      [:div
       (if-let [error @error] [:p "Error" error])
       [input-field message]
       [:button.btn.btn-primary
        {:on-click #(send-message! message error)}
        "send"]])))

(defn login-page []
  [:div
   [:label "login"]
   [input-field user]])

(defn home-page []
  [:div
   (if @user
     [:div.row
      [:div.col-sm-2 [side-bar]]
      [:div.col-md-4
       [message-list @messages]
       [message-component]]]
     [login-page])
   ])

(def pages
  {:home  home-page
   :about about-page})

(defn page []
  [(pages (session/get :page))])

(defroute "/" [] (session/put! :page :home))
(defroute "/about" [] (session/put! :page :about))

(defn mount-components []
  (reagent/render-component [navbar] (.getElementById js/document "navbar"))
  (reagent/render-component [page] (.getElementById js/document "app")))

(defn init! []
  (secretary/set-config! :prefix "#")
  (session/put! :page :home)
  (mount-components))


