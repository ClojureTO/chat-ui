(defproject chat-ui "0.1.0-SNAPSHOT"

  :description "A sample project for building a chat UI using websockets"
  :url "https://github.com/ClojureTO/chat-ui"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "4.0.2"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.67"]
                 [environ "1.0.0"]
                 [compojure "1.3.4"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [ring "1.4.0"
                  :exclusions [ring/ring-jetty-adapter]]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.3"]
                 [bouncer "0.3.3"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [org.clojure/clojurescript "0.0-3308" :scope "provided"]
                 [org.clojure/tools.reader "0.9.2"]
                 [reagent "0.5.0"]
                 [cljsjs/react "0.13.3-0"]
                 [reagent-forms "0.5.4"]
                 [reagent-utils "0.1.5"]
                 [secretary "1.2.3"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljs-ajax "0.3.13"]
                 [http-kit "2.1.19"]]

  :min-lein-version "2.0.0"
  :uberjar-name "chat-ui.jar"
  :jvm-opts ["-server"]

  :main chat-ui.core

  :plugins [[lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [lein-cljsbuild "1.0.6"]]
  :clean-targets ^{:protect false} ["resources/public/js"]
  :cljsbuild
  {:builds
   {:app
    {:source-paths ["src-cljs"]
     :compiler
     {:output-dir "resources/public/js/out"
      :externs ["react/externs/react.js"]
      :optimizations :none
      :output-to "resources/public/js/app.js"
      :pretty-print true}}}}

  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
              :hooks [leiningen.cljsbuild]
              :cljsbuild
              {:jar true
               :builds
               {:app
                {:source-paths ["env/prod/cljs"]
                 :compiler {:optimizations :advanced :pretty-print false}}}}

             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.4.0"]
                        [pjstadig/humane-test-output "0.7.0"]
                        [lein-figwheel "0.3.7"]
                        [org.clojure/tools.nrepl "0.2.10"]]
         :plugins [[lein-figwheel "0.3.7"]]
          :cljsbuild
          {:builds
           {:app
            {:source-paths ["env/dev/cljs"] :compiler {:source-map true}}}}

         :figwheel
         {:http-server-root "public"
          :server-port 3449
          :nrepl-port 7002
          :css-dirs ["resources/public/css"]
          :ring-handler chat-ui.handler/app}

         ;;when set the application start the nREPL server on load

         :repl-options {:init-ns chat-ui.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev        true
               :nrepl-port 7001}}})
