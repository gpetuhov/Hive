package com.gpetuhov.android.hive.ui.epoxy.filter.controller

import com.airbnb.epoxy.EpoxyController
import com.gpetuhov.android.hive.presentation.presenter.FilterFragmentPresenter
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterAwards
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterBasics
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterContacts
import com.gpetuhov.android.hive.ui.epoxy.filter.models.filterOffers

class FilterListController(private val presenter: FilterFragmentPresenter) : EpoxyController()  {

    override fun buildModels() {
        filterBasics {
            id("filter_basics")

            showUsersOffersAll(presenter.isShowUsersOffersAll())
            onShowUsersOffersAllClick { presenter.showUsersOffersAll() }

            showUsersOnly(presenter.isShowUsersOnly())
            onShowUsersOnlyClick { presenter.showUsersOnly() }

            showOffersOnly(presenter.isShowOffersOnly())
            onShowOffersOnlyClick { presenter.showOffersOnly() }
        }

        filterOffers {
            id("filter_offers")

            freeOffersOnly(presenter.isFreeOffersOnly())
            onFreeOffersOnlyClick { freeOffersOnly -> presenter.freeOffersOnly(freeOffersOnly) }

            offersWithReviewsOnly(presenter.isOffersWithReviewsOnly())
            onOffersWithReviewsClick { offersWithReviewsOnly -> presenter.offersWithReviewsOnly(offersWithReviewsOnly) }
        }
        
        filterContacts { 
            id("filter_contacts")
            
            hasPhone(presenter.hasPhone())
            onHasPhoneClick { hasPhone -> presenter.setHasPhone(hasPhone) }

            hasEmail(presenter.hasEmail())
            onHasEmailClick { hasEmail -> presenter.setHasEmail(hasEmail) }

            hasSkype(presenter.hasSkype())
            onHasSkypeClick { hasSkype -> presenter.setHasSkype(hasSkype) }

            hasFacebook(presenter.hasFacebook())
            onHasFacebookClick { hasFacebook -> presenter.setHasFacebook(hasFacebook) }

            hasTwitter(presenter.hasTwitter())
            onHasTwitterClick { hasTwitter -> presenter.setHasTwitter(hasTwitter) }

            hasInstagram(presenter.hasInstagram())
            onHasInstagramClick { hasInstagram -> presenter.setHasInstagram(hasInstagram) }

            hasYoutube(presenter.hasYoutube())
            onHasYoutubeClick { hasYoutube -> presenter.setHasYoutube(hasYoutube) }

            hasWebsite(presenter.hasWebsite())
            onHasWebsiteClick { hasWebsite -> presenter.setHasWebsite(hasWebsite) }
        }

        filterAwards {
            id("filter_awards")

            // TODO: init with values from the presenter
            hasTextMaster(false)
            onHasTextMasterClick {
                // TODO
            }
        }
    }
}