(ns synthetic-event-test.core
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.core.async :as async :refer [>! <! alts! chan sliding-buffer put! close!]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def app-state (atom {:text "Hello world!"}))

(defn child-component [data owner]
  (reify

    om/IRenderState
    (render-state [_ {:keys [test-chan]}]
      (dom/div #js {:onMouseUp #(do
                                  (.log js/console %)
                                  (put! test-chan [:non-persisted %]))
                    :onMouseDown #(do
                                   (.log js/console %)
                                   (.persist %)
                                   (put! test-chan [:persisted %]))} "Child Component"))))

(defn parent-component [data owner]

  (reify
    om/IInitState
    (init-state [_]
      {:test-chan (chan)})

    om/IWillMount
    (will-mount [_]
      (let [test-chan (om/get-state owner :test-chan)]
        (go (while true
              (let [[key event] (<! test-chan)]
                (condp = key
                  :non-persisted (do (pr "non-persisted")
                                     (.log js/console event))
                  :persisted     (do (pr "persisted")
                                     (.log js/console event))))))))

    om/IRenderState
    (render-state [_ {:keys [test-chan]}]
      (dom/div nil
        (om/build child-component data {:state {:test-chan test-chan}})))))

(om/root
  parent-component
  app-state
  {:target (. js/document (getElementById "app"))})
