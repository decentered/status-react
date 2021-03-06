(ns status-im.ui.screens.add-new.open-dapp.views
  (:require-macros [status-im.utils.views :as views])
  (:require [status-im.ui.components.react :as react]
            [status-im.ui.screens.add-new.open-dapp.styles :as styles]
            [status-im.ui.components.status-bar.view :as status-bar]
            [status-im.ui.components.toolbar.view :as toolbar.view]
            [status-im.i18n :as i18n]
            [re-frame.core :as re-frame]
            [status-im.ui.components.action-button.action-button :as action-button]
            [status-im.ui.components.common.common :as components]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.contact.contact :as contact-view]
            [status-im.ui.components.chat-icon.screen :as chat-icon.screen]))

(defn render-row [row _ _]
  [contact-view/contact-view {:contact       row
                              :on-press      #(re-frame/dispatch [:navigate-to :dapp-description row])
                              :show-forward? true}])

(views/defview open-dapp []
  (views/letsubs [dapps [:all-dapp-with-url-contacts]
                  url-text (atom nil)]
    [react/keyboard-avoiding-view styles/main-container
     [status-bar/status-bar]
     [toolbar.view/simple-toolbar (i18n/label :t/open-dapp)]
     [components/separator]
     [react/view styles/enter-url
      [react/text-input {:on-change-text    #(reset! url-text %)
                         :on-submit-editing #(do
                                               (re-frame/dispatch [:navigate-to-clean :home])
                                               (re-frame/dispatch [:open-browser {:url @url-text}]))
                         :placeholder       (i18n/label :t/enter-url)
                         :style             styles/url-input}]]
     [react/text {:style styles/list-title}
      (i18n/label :t/selected-dapps)]
     [list/flat-list {:data                      dapps
                      :render-fn                 render-row
                      :default-separator?        true
                      :enableEmptySections       true
                      :keyboardShouldPersistTaps :always}]]))

(views/defview dapp-description []
  (views/letsubs [{:keys [name dapp-url] :as dapp} [:get :new/open-dapp]]
    [react/keyboard-avoiding-view styles/main-container
     [status-bar/status-bar]
     [toolbar.view/simple-toolbar]
     [react/view {:margin-top 24 :align-items :center}
      [chat-icon.screen/dapp-icon-browser dapp 56]
      [react/text {:style styles/dapp-name}
       name]
      [react/text {:style styles/dapp}
       (i18n/label :t/dapp)]]
     [react/view {:margin-top 24}
      [action-button/action-button {:label (i18n/label :t/open) :icon :icons/address
                                    :on-press #(do
                                                 (re-frame/dispatch [:navigate-to-clean :home])
                                                 (re-frame/dispatch [:open-dapp-in-browser dapp]))}]
      [components/separator {:margin-left 72}]]
     [react/view styles/description-container
      [react/text {:style styles/gray-label}
       (i18n/label :t/description)]
      [react/text {:style (merge styles/black-label {:padding-top 18})}]
      [components/separator {:margin-top 15}]
      [react/text {:style (merge styles/gray-label {:padding-top 18})}
       (i18n/label :t/url)]
      [react/text {:style (merge styles/black-label {:padding-top 14})}
       dapp-url]
      [components/separator {:margin-top 6}]]]))
