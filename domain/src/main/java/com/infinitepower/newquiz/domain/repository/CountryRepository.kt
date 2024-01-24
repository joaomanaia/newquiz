package com.infinitepower.newquiz.domain.repository

import com.infinitepower.newquiz.model.country.Country

interface CountryRepository {
    suspend fun getAllCountries(): List<Country>
}
