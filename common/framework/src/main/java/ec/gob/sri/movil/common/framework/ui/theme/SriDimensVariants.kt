package ec.gob.sri.movil.common.framework.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Variante Compact (pantallas pequeñas).
 */
val SriCompactDimens = SriDimens(
    screenPadding = 12.dp,
    cardRadius = 16.dp,
    itemRadius = 12.dp,

    spaceXS = 2.dp,
    spaceS = 6.dp,
    spaceM = 10.dp,
    spaceL = 14.dp,

    cardPadding = 12.dp,
    homeCardMinHeight = 110.dp,

    homeGridSpacing = 10.dp,
    homeBottomBarClearance = 80.dp,

    // ✅ nuevos tokens (compact)
    cardElevationLow = 2.dp,
    cardElevationMed = 5.dp,
    bannerElevation = 7.dp,
    searchElevation = 3.dp,

    homeSearchMinHeight = 52.dp,
    homeBannerMinHeight = 175.dp,

    homeIconTilePaddingH = 10.dp,
    homeIconTilePaddingV = 8.dp,
)

/**
 * Variante Tablet (pantallas grandes).
 */
val SriTabletDimens = SriDimens(
    screenPadding = 24.dp,
    cardRadius = 22.dp,
    itemRadius = 18.dp,

    spaceXS = 6.dp,
    spaceS = 10.dp,
    spaceM = 16.dp,
    spaceL = 20.dp,

    cardPadding = 20.dp,
    homeCardMinHeight = 150.dp,

    homeGridSpacing = 20.dp,
    homeBottomBarClearance = 120.dp,

    // ✅ nuevos tokens (tablet)
    homeGridMinCellSize = 220.dp,
    cardElevationLow = 3.dp,
    cardElevationMed = 8.dp,
    bannerElevation = 10.dp,
    searchElevation = 6.dp,

    homeSearchMinHeight = 60.dp,
    homeBannerMinHeight = 220.dp,

    homeIconTilePaddingH = 16.dp,
    homeIconTilePaddingV = 12.dp,
)