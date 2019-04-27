(ns ^:figwheel-hooks bughouse.core
  (:require
    [goog.dom :as gdom]
    [reagent.core :as r :refer [atom]]
    [bughouse.moves :refer [valid-moves]]))

;; define your app data so that it doesn't get over-written on reload
(defonce ^:const starting-position [["bR" "bH" "bE" "bA" "bG" "bA" "bE" "bH" "bR"]
                                ["" "" "" "" "" "" "" "" ""]
                                ["" "bC" "" "" "" "" "" "bC" ""]
                                ["bP" "" "bP" "" "bP" "" "bP" "" "bP"]
                                ["" "" "" "" "" "" "" "" ""]
                                ["" "" "" "" "" "" "" "" ""]
                                ["rP" "" "rP" "" "rP" "" "rP" "" "rP"]
                                ["" "rC" "" "" "" "" "" "rC" ""]
                                ["" "" "" "" "" "" "" "" ""]
                                ["rR" "rH" "rE" "rA" "rG" "rA" "rE" "rH" "rR"]])

(defonce state (r/atom {:position    starting-position
                        :game-state  :waiting-for-players
                        :players     {}
                        :turn-colour :r}))

(defonce board-position (r/cursor state [:position]))

(defonce selected (r/cursor state [:selected]))

(defonce turn-colour (r/cursor state [:turn-colour]))

(defn handle-click
  [state i j]
  (cond
    (= (:selected @state) [i j]) (swap! state assoc :selected nil)
    (not= "" (get-in (:position @state) [i j])) (swap! state assoc :selected [i j])))

(defn take-seat
  [colour]
  (let [name (.prompt js/window "What is your name?")]
    (swap! state
           (fn [state colour name] (let [state (assoc-in state [:players colour] name)
                                         players (:players state)]
                                     (case (count players)
                                       1 (assoc state :game-state :waiting-for-other-player)
                                       2 (assoc state :game-state :in-progress))))
           colour name)))

;; -------------------------
;; Views

(defn square
  [piece selected move-allowed on-click]
  (let [attrs {:class "square" :data-piece piece :on-click on-click}
        attrs (if (not= "" piece) (assoc attrs :data-side (first piece)) attrs)
        attrs (if selected (assoc attrs :data-selected true) attrs)
        attrs (if move-allowed (assoc attrs :data-selected true) attrs)]
    [:div attrs]))

(defn board
  [position selected]
  (let [selected-coords @selected
        allowed-moves (valid-moves @position selected-coords)]
    [:div {:class "board" :data-turn @turn-colour :data-action (if (nil? selected-coords) "picking" "placing")}
     (map-indexed
       (fn [i row]
         (map-indexed (fn [j piece]
                        ^{:key [i, j, piece]} [square piece (= selected-coords [i j]) (allowed-moves [i j]) #(handle-click state i j)])
                      row))
       @position)]))

(defn seat
  [colour]
  [:div {:class "seat"}
   [:div (case colour :red "Red" :black "Black")]
   (if-let [name (get-in @state [:players colour])]
     name
     (case (:game-state @state)
       :waiting-for-players [:button {:on-click #(take-seat colour)} "Take seat"]
       :waiting-for-other-player [:button {:disabled true} "Waiting for other player"]))])

(defn table
  []
  [:div {:class "table"}
   [:div [seat :red] [seat :black]]
   [board board-position selected turn-colour]])

(defn app
  []
  [table])

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
