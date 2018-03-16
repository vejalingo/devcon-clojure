(ns devcon.core
  (:require [reagent.core :as r]))

;;Watches data
(def watches-arr [{:make "swatch classic" :price 500 :warranty 3}
                  {:make "swatch classic" :price 500 :warranty 3}
                  {:make "rolex gold" :price 800 :warranty 3}
                  {:make "swatch sport" :price 400 :warranty 3}
                  {:make "swatch sport" :price 200 :warranty 3}
                  {:make "rolex gold" :price 400 :warranty 3}])

(def watch-make (r/atom nil))
(def watch-warranty (r/atom 3))
(def watch-price-low (r/atom 0))
(def watch-price-high (r/atom 0))

;;filters conditions
(defn by-make [make] #(= (:make %) make))
(defn by-warranty [warranty] #(= (:warranty %) warranty))
(defn by-price [price-low price-high]
  #(and (>= (:price %) price-low)
        (<= (:price %) price-high)))

(defn make-input []
  [:select {:class "form-control" :id "watch-make" :on-change #(reset! watch-make (-> % .-target .-value))}
    [:option {:value "swatch classic" :selected "selected"} "Swatch classic"]
    [:option {:value "rolex gold"} "Rolex gold"]
    [:option {:value "swatch sport"} "Swatch sport"]])

(defn warranty-input []
  [:select {:class "form-control" :id "watch-warranty" :on-change #(reset! watch-warranty (-> % .-target .-value))}
    [:option {:value "1"} "1 years"]
    [:option {:value "3" :selected "selected"} "3 years"]
    [:option {:value "6"} "6 years"]])

(defn price-slider [the-price]
  [:input {:type "range" :value @the-price :min "0" :max "1000"
          :style {:width "100%"}
          :on-change #(reset! the-price (-> % .-target .-value))}])

(defn watch-form []
  [:form
    [:div.form-group
      [:label (str "Make: " @watch-make)]
      [make-input]
      [:label (str "Warranty: " @watch-warranty " years")]
      [warranty-input]
      [:label (str "Price Min: R " @watch-price-low ".00")]
      [price-slider watch-price-low]
      [:label (str "Price Max: R " @watch-price-high ".00")]
      [price-slider watch-price-high]]])

;; query watches logic
(defn query-watches []
  (->> watches-arr 
    (filter (by-make @watch-make))
    (filter (by-warranty @watch-warranty))
    (filter (by-price @watch-price-low @watch-price-high))))

(defn output-ui []
  [:div
  (for [result (into [] (query-watches))]
    (let [{:keys [make price warranty]} result]
        [:div.card.mb-4.box-shadow
          [:div.card-header
          [:h4.my-0.font-weight-normal
            (str make)]]
          [:div.card-body
          [:h1.card-title.pricing-card-title (str "R " price ".00")]
          [:ul.list-unstyled.mt-3.mb-4
            [:li (str warranty " years warranty")]
            [:li "............"]
            [:li "..........."]]
          [:button.btn.btn-lg.btn-block.btn-outline-primary
            {:type "button"}
            "Purchase"]]]))])
  
(defn home-page []
  [:div [:h2 "Devcon Watch finder"]
    [watch-form]
    [output-ui]])

;;(.log js/console (into [] (query-watches)))

;; -------------------------
;; Initialize app
(defn mount-root []
(r/render [home-page] (.getElementById js/document "app")))

(defn init! []
(mount-root))
