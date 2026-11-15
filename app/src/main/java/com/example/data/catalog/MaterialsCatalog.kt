package com.example.data.catalog

import com.example.data.model.MaterialCategory
import com.example.data.model.MaterialItem

object MaterialsCatalog {
    val items: List<MaterialItem> = listOf(
        MaterialItem("mat_1", 1, "بلدوزر", "يومية", 620_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_2", 2, "شفل", "يومية", 387_500.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_3", 3, "قلاب", "يومية", 200_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_4", 4, "كريدر", "يومية", 465_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_5", 5, "حفارة + جك", "يومية", 465_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_6", 6, "حفارة جي سي", "يومية", 387_500.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_7", 7, "حادلة", "يومية", 251_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_8", 8, "تانكر ماء", "يومية", 180_000.0, MaterialCategory.EQUIPMENT),
        MaterialItem("mat_9", 9, "حجر مكسر R7", "قلاب", 155_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_10", 10, "حصى خابط صنف A", "قلاب", 150_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_11", 11, "رمل البناء", "قلاب", 230_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_12", 12, "سمنت", "طن", 118_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_13", 13, "حصو", "قلاب", 140_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_14", 14, "كونكريت C25", "م3", 69_750.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_15", 15, "قوالب وسطية مع النقل", "عدد", 4_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_16", 16, "قوالب جانبية مع النقل", "عدد", 5_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_17", 17, "بي ار سي 6 ملم 15*15 سم", "م2", 4_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_18", 18, "نجار قالب خشبي", "م2", 7_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_19", 19, "بلوك صلد مع النقل", "عدد", 500.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_20", 20, "بوري كوركيت 50 سم", "م.ط", 60_000.0, MaterialCategory.PIPES),
        MaterialItem("mat_21", 21, "قباغ 40*60 سم", "عدد", 100_000.0, MaterialCategory.PIPES),
        MaterialItem("mat_22", 22, "حديد التسليح", "طن", 1_023_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_23", 23, "بوري كونكريتي مسلح 90 سم M3", "م.ط", 55_000.0, MaterialCategory.PIPES),
        MaterialItem("mat_24", 24, "بوري كونكريتي مسلح 45", "م.ط", 30_000.0, MaterialCategory.PIPES),
        MaterialItem("mat_25", 25, "قلاب حجر للبناء", "قلاب", 160_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_26", 26, "بناء حجر", "م3", 25_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_27", 27, "رمل الحدائق", "قلاب", 150_000.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_28", 28, "حداد حديد التسليح", "طن", 155_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_29", 29, "بناء كيربستون جانبي", "م.ط", 2_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_30", 30, "بناء كيربستون وسطي", "م.ط", 2_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_31", 31, "بناء بالبلوك الصلد", "م3", 17_000.0, MaterialCategory.LABOR),
        MaterialItem("mat_32", 32, "لباخ مع العمال", "م2", 3_500.0, MaterialCategory.LABOR),
        MaterialItem("mat_33", 33, "قباغ المنهول قطر 60سم", "عدد", 170_000.0, MaterialCategory.PIPES),
        MaterialItem("mat_34", 34, "جوينات باستعمال القاطع الكهربائي", "م2", 500.0, MaterialCategory.LABOR),
        MaterialItem("mat_35", 35, "بي ار سي 5 ملم 7*7 سم", "م2", 6_500.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_36", 36, "بي ار سي 5 ملم 5*5 سم", "م2", 7_500.0, MaterialCategory.STRUCTURAL),
        MaterialItem("mat_37", 37, "جلمود", "قلاب", 150_000.0, MaterialCategory.STRUCTURAL)
    )

    val byId: Map<String, MaterialItem> = items.associateBy { it.id }
}
