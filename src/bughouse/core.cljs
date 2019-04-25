(ns ^:figwheel-hooks bughouse.core
  (:require
    [goog.dom :as gdom]
    [reagent.core :as r :refer [atom]]))

(println "This text is printed from src/hello_world/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))


;; define your app data so that it doesn't get over-written on reload
(def ^:const starting-position [["bR" "bH" "bE" "bA" "bG" "bA" "bE" "bH" "bR"]
                                ["" "" "" "" "" "" "" "" ""]
                                ["" "bC" "" "" "" "" "" "bC" ""]
                                ["bP" "" "bP" "" "bP" "" "bP" "" "bP"]
                                ["" "" "" "" "" "" "" "" ""]
                                ["" "" "" "" "" "" "" "" ""]
                                ["rP" "" "rP" "" "rP" "" "rP" "" "rP"]
                                ["" "rC" "" "" "" "" "" "rC" ""]
                                ["" "" "" "" "" "" "" "" ""]
                                ["rR" "rH" "rE" "rA" "rG" "rA" "rE" "rH" "rR"]])

(defonce state (r/atom {:position starting-position :turn-colour :r}))

(defonce board-position (r/cursor state [:position]))

(defonce selected (r/cursor state [:selected]))

(defonce turn-colour (r/cursor state [:turn-colour]))

(defn handle-click
  [position selected i j]
  (cond
    (= @selected [i j]) (reset! selected nil)
    (not= "" (get-in @position [i j])) (reset! selected [i j])))

;; -------------------------
;; Views

(defn square
  [piece selected on-click]
  (let [attrs {:class "square" :data-piece piece :on-click on-click}
        attrs (if (not= "" piece) (assoc attrs :data-side (first piece)) attrs)
        attrs (if selected (assoc attrs :data-selected true) attrs)]
    [:div attrs]))

(defn board
  [position selected]
  (let [selected-coords @selected]
    [:div {:class "board" :data-turn @turn-colour :data-action (if (nil? selected-coords) "picking" "placing")}
     (map-indexed
       (fn [i row]
         (map-indexed (fn [j piece]
                        ^{:key [i, j, piece]} [square piece (= selected-coords [i j]) #(handle-click position selected i j)])
                      row))
       @position)]))

(defn app
  []
  [:div {:class "app"} [board board-position selected turn-colour]])

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (r/render-component [app] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
