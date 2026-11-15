package com.example.data.catalog

import com.example.data.model.AnalysisLineSpec
import com.example.data.model.ItemCategory
import com.example.data.model.LineFormula.Fixed
import com.example.data.model.LineFormula.FromMaterial
import com.example.data.model.LineFormula.FromWorkItem
import com.example.data.model.ProfitRule
import com.example.data.model.WorkItemSpec

object WorkItemsCatalogPart1 {
    val items: List<WorkItemSpec> = listOf(
        // 1. التسوية الترابية
        WorkItemSpec(
            id = "item_1",
            summaryOrder = 1,
            displayNumber = "1",
            title = "التسوية الترابية لكل م2",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            textOnlyPrice = "حسب الكمية والموقع بحيث لا تقل عن 1000 دينار"
        ),

        // 2. الدفن بالتراب النظيف بسمك 25سم
        WorkItemSpec(
            id = "item_2",
            summaryOrder = 2,
            displayNumber = "2",
            title = "الدفن بالتراب النظيف بسمك 25سم",
            unit = "م3",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("i2_1", "بلدوزر", 1.0, "م3", FromMaterial("mat_1", divisor = 300.0)),
                AnalysisLineSpec("i2_2", "شفل", 1.0, "م3", FromMaterial("mat_2", divisor = 250.0)),
                AnalysisLineSpec("i2_3", "قلاب", 1.0, "م3", FromMaterial("mat_3", divisor = 125.0)),
                AnalysisLineSpec("i2_4", "كريدر", 1.0, "م3", FromMaterial("mat_4", divisor = 500.0)),
                AnalysisLineSpec("i2_5", "حادلة", 1.0, "م3", FromMaterial("mat_7", divisor = 500.0)),
                AnalysisLineSpec("i2_6", "تانكر", 1.0, "م3", FromMaterial("mat_8", divisor = 500.0)),
                AnalysisLineSpec("i2_7", "فحوصات مختبرية", 1.0, "جملة", Fixed(600.0))
            )
        ),

        // 3. قطع التربة الغير صخرية مع رفع الانقاض
        WorkItemSpec(
            id = "item_3",
            summaryOrder = 3,
            displayNumber = "3",
            title = "قطع التربة الغير صخرية مع رفع الانقاض",
            unit = "م3",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("i3_1", "بلدوزر", 1.0, "م3", FromMaterial("mat_1", divisor = 250.0)),
                AnalysisLineSpec("i3_2", "شفل", 1.0, "م3", FromMaterial("mat_2", divisor = 250.0)),
                AnalysisLineSpec("i3_3", "قلاب", 1.0, "م3", FromMaterial("mat_3", divisor = 125.0))
            )
        ),

        // 4. قطع التربة الصخرية مع رفع الانقاض
        WorkItemSpec(
            id = "item_4",
            summaryOrder = 4,
            displayNumber = "4",
            title = "قطع التربة الصخرية مع رفع الانقاض",
            unit = "م3",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("i4_1", "حفارة جك", 1.0, "م3", FromMaterial("mat_5", divisor = 100.0)),
                AnalysisLineSpec("i4_2", "شفل", 1.0, "م3", FromMaterial("mat_2", divisor = 200.0)),
                AnalysisLineSpec("i4_3", "قلاب", 1.0, "م3", FromMaterial("mat_3", divisor = 100.0))
            )
        ),

        // Base analysis (Page 1 table 5): تبديل التربة سمك 15سم -> feeds Item 5 (30cm)
        WorkItemSpec(
            id = "base_soil_15",
            summaryOrder = -1,
            displayNumber = "تحليل",
            title = "تبديل التربة سمك 15سم (طبقة واحدة)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("bs15_1", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 2000.0), "قطع التربة مع رفع الانقاض"),
                AnalysisLineSpec("bs15_2", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 2000.0)),
                AnalysisLineSpec("bs15_3", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs15_4", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 2000.0), "الدفن بالتربة مع الفرش والحدل"),
                AnalysisLineSpec("bs15_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 2000.0)),
                AnalysisLineSpec("bs15_6", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs15_7", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 2000.0)),
                AnalysisLineSpec("bs15_8", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 2000.0)),
                AnalysisLineSpec("bs15_9", "تانكر", 1.0, "م2", FromMaterial("mat_8", divisor = 2000.0)),
                AnalysisLineSpec("bs15_10", "فحوصات", 1.0, "جملة", Fixed(190.0))
            ),
            extraNotes = listOf("تبديل التربة لشوارع سمك 30سم" to 2.0)
        ),

        // 5. تبديل التربة سمك 30 سم (2 طبقة 15+15)
        WorkItemSpec(
            id = "item_5",
            summaryOrder = 5,
            displayNumber = "5",
            title = "تبديل التربة سمك 30 سم (2 طبقة 15+15)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            multiplierFromWorkItemId = "base_soil_15",
            multiplierFactor = 2.0
        ),

        // Base analysis (Page 2 table 1): تبديل التربة سمك 20سم -> feeds Item 6 (40cm)
        WorkItemSpec(
            id = "base_soil_20",
            summaryOrder = -1,
            displayNumber = "تحليل",
            title = "تبديل التربة سمك 20سم (طبقة واحدة)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("bs20_1", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 1500.0), "قطع التربة مع رفع الانقاض"),
                AnalysisLineSpec("bs20_2", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1500.0)),
                AnalysisLineSpec("bs20_3", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs20_4", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 1500.0), "الدفن بالتربة مع الفرش والحدل"),
                AnalysisLineSpec("bs20_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1500.0)),
                AnalysisLineSpec("bs20_6", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs20_7", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 1500.0)),
                AnalysisLineSpec("bs20_8", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 1500.0)),
                AnalysisLineSpec("bs20_9", "تانكر", 1.0, "م2", FromMaterial("mat_8", divisor = 1500.0)),
                AnalysisLineSpec("bs20_10", "فحوصات", 1.0, "جملة", Fixed(190.0))
            ),
            extraNotes = listOf(
                "تبديل التربة لشوارع سمك 40سم" to 2.0,
                "تبديل التربة لشوارع سمك 60سم" to 3.0
            )
        ),

        // 6. تبديل التربة سمك 40سم (2طبقة 20+20)
        WorkItemSpec(
            id = "item_6",
            summaryOrder = 6,
            displayNumber = "6",
            title = "تبديل التربة سمك 40سم (2طبقة 20+20)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            multiplierFromWorkItemId = "base_soil_20",
            multiplierFactor = 2.0
        ),

        // Base analysis (Page 2 table 2): تبديل التربة سمك 25سم -> feeds Item 7 (50cm)
        WorkItemSpec(
            id = "base_soil_25",
            summaryOrder = -1,
            displayNumber = "تحليل",
            title = "تبديل التربة سمك 25سم (طبقة واحدة)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            lines = listOf(
                AnalysisLineSpec("bs25_1", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 1100.0), "قطع التربة مع رفع الانقاض"),
                AnalysisLineSpec("bs25_2", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1100.0)),
                AnalysisLineSpec("bs25_3", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs25_4", "بلدوزر", 1.0, "م2", FromMaterial("mat_1", divisor = 1100.0), "الدفن بالتربة مع الفرش والحدل"),
                AnalysisLineSpec("bs25_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1100.0)),
                AnalysisLineSpec("bs25_6", "قلاب", 1.0, "م2", FromMaterial("mat_3", divisor = 600.0)),
                AnalysisLineSpec("bs25_7", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 1200.0)),
                AnalysisLineSpec("bs25_8", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 1200.0)),
                AnalysisLineSpec("bs25_9", "تانكر", 1.0, "م2", FromMaterial("mat_8", divisor = 1200.0)),
                AnalysisLineSpec("bs25_10", "فحوصات", 1.0, "جملة", Fixed(160.0))
            ),
            extraNotes = listOf("تبديل التربة لشوارع سمك 50سم" to 2.0)
        ),

        // 7. تبديل التربة سمك 50سم (2 طبقة 25+25)
        WorkItemSpec(
            id = "item_7",
            summaryOrder = 7,
            displayNumber = "7",
            title = "تبديل التربة سمك 50سم (2 طبقة 25+25)",
            unit = "م2",
            category = ItemCategory.EARTHWORK,
            multiplierFromWorkItemId = "base_soil_25",
            multiplierFactor = 2.0
        ),

        // Base analysis (Page 2 table 3): حصى خابط صنف A سمك 15 سم -> feeds Item 8 (30cm)
        WorkItemSpec(
            id = "base_subbase_15",
            summaryOrder = -1,
            displayNumber = "تحليل",
            title = "حصى خابط صنف A سمك 15 سم (طبقة واحدة)",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            lines = listOf(
                AnalysisLineSpec("bsb15_1", "تجهيز", 1.0, "م2", FromMaterial("mat_10", divisor = 46.4)),
                AnalysisLineSpec("bsb15_2", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 2000.0)),
                AnalysisLineSpec("bsb15_3", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 2000.0)),
                AnalysisLineSpec("bsb15_4", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 2000.0)),
                AnalysisLineSpec("bsb15_5", "تانكر", 1.0, "م2", FromMaterial("mat_8", divisor = 2000.0)),
                AnalysisLineSpec("bsb15_6", "فحوصات", 1.0, "جملة", Fixed(150.0))
            ),
            extraNotes = listOf("حصى خابط صنف A سمك 30 سم" to 2.0)
        ),

        // 8. حصى خابط صنف A سمك 30 سم ( 2طبقة 15+15)
        WorkItemSpec(
            id = "item_8",
            summaryOrder = 8,
            displayNumber = "8",
            title = "حصى خابط صنف A سمك 30 سم (2 طبقة 15+15)",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            multiplierFromWorkItemId = "base_subbase_15",
            multiplierFactor = 2.0
        ),

        // 9. حصى خابط صنف A سمك 20 سم
        WorkItemSpec(
            id = "item_9",
            summaryOrder = 9,
            displayNumber = "9",
            title = "حصى خابط صنف A سمك 20 سم",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            lines = listOf(
                AnalysisLineSpec("i9_1", "تجهيز", 1.0, "م2", FromMaterial("mat_10", divisor = 34.8)),
                AnalysisLineSpec("i9_2", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 1500.0)),
                AnalysisLineSpec("i9_3", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 1500.0)),
                AnalysisLineSpec("i9_4", "تانكر", 1.0, "م2", FromMaterial("mat_8", divisor = 1500.0)),
                AnalysisLineSpec("i9_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1500.0)),
                AnalysisLineSpec("i9_6", "فحوصات", 1.0, "جملة", Fixed(250.0))
            )
        ),

        // Base analysis (Page 3 table 2): حجر مكسر سمك 15 سم -> feeds Item 10 (30cm)
        WorkItemSpec(
            id = "base_crushed_15",
            summaryOrder = -1,
            displayNumber = "تحليل",
            title = "حجر مكسر سمك 15 سم (طبقة واحدة)",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            lines = listOf(
                AnalysisLineSpec("bc15_1", "تجهيز", 1.0, "م2", FromMaterial("mat_9", divisor = 46.4)),
                AnalysisLineSpec("bc15_2", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 2000.0)),
                AnalysisLineSpec("bc15_3", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 2000.0)),
                AnalysisLineSpec("bc15_4", "تانكر ماء", 1.0, "م2", FromMaterial("mat_8", divisor = 2000.0)),
                AnalysisLineSpec("bc15_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 2000.0)),
                AnalysisLineSpec("bc15_6", "فحوصات", 1.0, "جملة", Fixed(150.0))
            ),
            extraNotes = listOf("المبلغ النهائي لسمك 30 سم" to 2.0)
        ),

        // 10. حجر مكسر سمك 30 سم ( 2طبقة 15+15)
        WorkItemSpec(
            id = "item_10",
            summaryOrder = 10,
            displayNumber = "10",
            title = "حجر مكسر سمك 30 سم (2 طبقة 15+15)",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            multiplierFromWorkItemId = "base_crushed_15",
            multiplierFactor = 2.0
        ),

        // 11. حجر مكسر سمك 20 سم
        WorkItemSpec(
            id = "item_11",
            summaryOrder = 11,
            displayNumber = "11",
            title = "حجر مكسر سمك 20 سم",
            unit = "م2",
            category = ItemCategory.SUBBASE,
            lines = listOf(
                AnalysisLineSpec("i11_1", "تجهيز", 1.0, "م2", FromMaterial("mat_9", divisor = 34.8)),
                AnalysisLineSpec("i11_2", "كريدر", 1.0, "م2", FromMaterial("mat_4", divisor = 1500.0)),
                AnalysisLineSpec("i11_3", "حادلة", 1.0, "م2", FromMaterial("mat_7", divisor = 1500.0)),
                AnalysisLineSpec("i11_4", "تانكر ماء", 1.0, "م2", FromMaterial("mat_8", divisor = 1500.0)),
                AnalysisLineSpec("i11_5", "شفل", 1.0, "م2", FromMaterial("mat_2", divisor = 1500.0)),
                AnalysisLineSpec("i11_6", "فحوصات", 1.0, "جملة", Fixed(250.0))
            )
        ),

        // 12. القوالب الجانبية
        WorkItemSpec(
            id = "item_12",
            summaryOrder = 12,
            displayNumber = "12",
            title = "القوالب الجانبية",
            unit = "م.ط",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i12_1", "تجهيز", 1.0, "م.ط", FromMaterial("mat_16", divisor = 0.75)),
                AnalysisLineSpec("i12_2", "صب الاساس", 0.128, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i12_3", "صب الكيرب", 0.045, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i12_4", "صب خلف الكيربستون", 0.0225, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i12_5", "نجار (كيرب + خلف القالب + قاعدة)", 4.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i12_6", "بناء", 1.0, "م.ط", FromMaterial("mat_29")),
                AnalysisLineSpec("i12_7", "رمل", 0.0085, "م3", FromMaterial("mat_11", divisor = 8.0)),
                AnalysisLineSpec("i12_8", "سمنت", 8.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i12_9", "تجهيز ماء للمونة + الرش", 1.0, "م.ط", Fixed(250.0)),
                AnalysisLineSpec("i12_10", "دفن الارصفة والجزرة الوسطية بالتراب", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i12_11", "فحوصات", 1.0, "جملة", Fixed(700.0))
            )
        ),

        // 13. صب القوالب الوسطية موقعيا
        WorkItemSpec(
            id = "item_13",
            summaryOrder = 13,
            displayNumber = "13",
            title = "صب القوالب الوسطية موقعيا (طول=50سم، عرض=40سم، سمك=20سم)",
            summaryTitle = "صب القوالب الوسطية موقعيا (عرض=40سم، سمك=25سم)",
            unit = "م.ط",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i13_1", "حفريات", 1.0, "م.ط", FromMaterial("mat_6", divisor = 450.0)),
                AnalysisLineSpec("i13_2", "نجار", 1.0, "م.ط", FromMaterial("mat_30", amountMultiplier = 2.0)),
                AnalysisLineSpec("i13_3", "صب الاساس", 0.1, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i13_4", "تجهيز ماء الرش", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i13_5", "فحوصات", 1.0, "جملة", Fixed(100.0))
            )
        ),

        // 14. العبارات بسمك 25سم
        WorkItemSpec(
            id = "item_14",
            summaryOrder = 14,
            displayNumber = "14",
            title = "العبارات بسمك 25سم",
            unit = "م2",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i14_1", "صب", 0.3, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i14_2", "حديد تسليح 10 ملم", 8.82, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i14_3", "قالب الخشبي", 2.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i14_4", "بناء", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i14_5", "رش", 1.0, "م2", Fixed(500.0))
            )
        ),

        // 15. صب الشوارع بسمك 20سم
        WorkItemSpec(
            id = "item_15",
            summaryOrder = 15,
            displayNumber = "15",
            title = "صب الشوارع بسمك 20سم مع استعمال شبكة B.R.C 6 ملم 15*15 سم",
            unit = "م2",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i15_1", "صب", 0.2, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i15_2", "B.R.C", 1.0, "م2", FromMaterial("mat_17")),
                AnalysisLineSpec("i15_3", "بناء", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i15_4", "جوينات باستعمال القاطع الكهربائي", 1.0, "م2", FromMaterial("mat_34")),
                AnalysisLineSpec("i15_5", "رش", 1.0, "م2", Fixed(500.0)),
                AnalysisLineSpec("i15_6", "فحوصات", 1.0, "جملة", Fixed(150.0))
            )
        ),

        // 16. صب الشوارع بسمك 15سم
        WorkItemSpec(
            id = "item_16",
            summaryOrder = 16,
            displayNumber = "16",
            title = "صب الشوارع بسمك 15سم مع استعمال شبكة B.R.C 6 ملم",
            unit = "م2",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i16_1", "صب", 0.15, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i16_2", "B.R.C", 1.0, "م2", FromMaterial("mat_17")),
                AnalysisLineSpec("i16_3", "بناء", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i16_4", "جوينات باستعمال القاطع الكهربائي", 1.0, "م2", FromMaterial("mat_34")),
                AnalysisLineSpec("i16_5", "رش", 1.0, "م2", Fixed(500.0)),
                AnalysisLineSpec("i16_6", "فحوصات", 1.0, "جملة", Fixed(150.0))
            )
        ),

        // 17. صب الأرصفة وصيانة الشوارع 12سم غير مسلح
        WorkItemSpec(
            id = "item_17",
            summaryOrder = 17,
            displayNumber = "17",
            title = "صب الأرصفة وصيانة الشوارع (صبة جديدة فوق صبة قديمة بسمك 12سم غير مسلح)",
            unit = "م2",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i17_1", "صب", 0.12, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i17_2", "بناء", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i17_3", "جوينات باستعمال القاطع الكهربائي", 1.0, "م2", FromMaterial("mat_34")),
                AnalysisLineSpec("i17_4", "رش", 1.0, "م2", Fixed(500.0)),
                AnalysisLineSpec("i17_5", "فحوصات", 1.0, "جملة", Fixed(250.0))
            )
        ),

        // 18. البناء بالبلوك الصلد (feeds items 21, 23, 24, 32!)
        WorkItemSpec(
            id = "item_18",
            summaryOrder = 18,
            displayNumber = "18",
            title = "البناء بالبلوك الصلد",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i18_1", "تجهيز", 80.0, "بلوك", FromMaterial("mat_19")),
                AnalysisLineSpec("i18_2", "بناء مع العمال", 1.0, "م3", FromMaterial("mat_31")),
                AnalysisLineSpec("i18_3", "رمل", 0.15, "م3", FromMaterial("mat_11", divisor = 9.0)),
                AnalysisLineSpec("i18_4", "سمنت", 75.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i18_5", "تجهيز ماء مع الرش", 1.0, "م3", Fixed(1000.0)),
                AnalysisLineSpec("i18_6", "فحوصات مختبرية", 1.0, "جملة", Fixed(2500.0))
            )
        ),

        // 19. لبخ الجدران بسمك 2 سم (feeds items 21, 23, 24, 32!)
        WorkItemSpec(
            id = "item_19",
            summaryOrder = 19,
            displayNumber = "19",
            title = "لبخ الجدران بسمك 2 سم",
            unit = "م2",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i19_1", "لباخ مع العمال", 1.0, "م2", FromMaterial("mat_32")),
                AnalysisLineSpec("i19_2", "رمل", 0.025, "م3", FromMaterial("mat_11", divisor = 9.0)),
                AnalysisLineSpec("i19_3", "سمنت", 14.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i19_4", "تجهيز ماء مع الرش", 1.0, "م2", Fixed(750.0))
            )
        ),

        // 20. مجاري كوريكيت بقطر 50سم
        WorkItemSpec(
            id = "item_20",
            summaryOrder = 20,
            displayNumber = "20",
            title = "مجاري كوريكيت بقطر 50سم",
            unit = "م.ط",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i20_1", "حفريات", 1.25, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i20_2", "تجهيز بوري", 1.0, "م.ط", FromMaterial("mat_20")),
                AnalysisLineSpec("i20_3", "تركيب بوري", 1.0, "م.ط", Fixed(5000.0)),
                AnalysisLineSpec("i20_4", "رمل الحدائق + تسوية وتعديل بالعمال", 0.5, "م3", FromMaterial("mat_27", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i20_5", "دفن بالتراب النظيف", 0.5, "م3", FromWorkItem("item_2"))
            )
        ),

        // 21. فوهات الاستلام (40*60)سم
        WorkItemSpec(
            id = "item_21",
            summaryOrder = 21,
            displayNumber = "21",
            title = "فوهات الاستلام (40*60)سم",
            unit = "عدد",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i21_1", "حفريات", 0.8, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i21_2", "حجر مكسر + عمال", 0.1, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i21_3", "صب القاعدة", 0.1, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i21_4", "بلوك", 0.5, "م3", FromWorkItem("item_18")),
                AnalysisLineSpec("i21_5", "لبخ", 2.0, "م2", FromWorkItem("item_19")),
                AnalysisLineSpec("i21_6", "فلينكوت", 2.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i21_7", "قباغ (0.4*0.6)", 1.0, "عدد", FromMaterial("mat_21"))
            )
        ),

        // 22. قنطرة صندوقية بابعاد (2*2)م جدار كونكريت مسلح
        WorkItemSpec(
            id = "item_22",
            summaryOrder = 22,
            displayNumber = "22",
            title = "قنطرة صندوقية بابعاد (2*2)م جدار كونكريت مسلح",
            unit = "م.ط",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i22_1", "حفريات", 25.6, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i22_2", "حجر مكسر + عمال", 0.25, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i22_3", "صب القاعدة والجدران والسقف", 2.75, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i22_4", "صب الاجنحة", 0.5, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i22_5", "نجار", 28.0, "م2", FromMaterial("mat_18")),
                AnalysisLineSpec("i22_6", "حديد تسليح", 425.0, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i22_7", "حداد", 425.0, "كغم", FromMaterial("mat_28", divisor = 1000.0)),
                AnalysisLineSpec("i22_8", "water stop", 1.0, "م.ط", Fixed(20_000.0)),
                AnalysisLineSpec("i22_9", "الدفن", 20.0, "م3", FromWorkItem("item_2")),
                AnalysisLineSpec("i22_10", "الرش", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i22_11", "الفحوصات", 1.0, "م.ط", Fixed(20_000.0))
            )
        ),

        // 23. مجاري صندوقي بابعاد (2*2)م جدار بلوك
        WorkItemSpec(
            id = "item_23",
            summaryOrder = 23,
            displayNumber = "23",
            title = "مجاري صندوقي بابعاد (2*2)م جدار بلوك",
            unit = "م.ط",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i23_1", "حفريات", 9.0, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i23_2", "حجر مكسر + عمال", 0.5, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i23_3", "صب القاعدة", 0.8, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i23_4", "صب السقف", 0.58, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i23_5", "نجار", 5.0, "م2", Fixed(6500.0)),
                AnalysisLineSpec("i23_6", "حديد تسليح", 60.0, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i23_7", "حداد", 60.0, "كغم", FromMaterial("mat_28", divisor = 1000.0)),
                AnalysisLineSpec("i23_8", "بلوك", 1.5, "م3", FromWorkItem("item_18")),
                AnalysisLineSpec("i23_9", "لبخ", 4.0, "م2", FromWorkItem("item_19")),
                AnalysisLineSpec("i23_10", "فلينكوت", 4.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i23_11", "الدفن بالتراب", 3.6, "م3", FromWorkItem("item_2")),
                AnalysisLineSpec("i23_12", "الرش", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i23_13", "الفحوصات", 1.0, "م.ط", Fixed(10_000.0))
            )
        ),

        // 24. منهولات بابعاد (2*2)م
        WorkItemSpec(
            id = "item_24",
            summaryOrder = 24,
            displayNumber = "24",
            title = "منهولات بابعاد (2*2)م",
            unit = "عدد",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i24_1", "حفريات", 18.6, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i24_2", "حجر مكسر + عمال", 0.6, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i24_3", "صب القاعدة", 1.55, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i24_4", "صب السقف", 1.15, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i24_5", "نجار", 15.5, "م2", Fixed(6500.0)),
                AnalysisLineSpec("i24_6", "حديد تسليح", 120.0, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i24_7", "حداد", 120.0, "كغم", FromMaterial("mat_28", divisor = 1000.0)),
                AnalysisLineSpec("i24_8", "بلوك", 3.1, "م3", FromWorkItem("item_18")),
                AnalysisLineSpec("i24_9", "لبخ", 8.0, "م2", FromWorkItem("item_19")),
                AnalysisLineSpec("i24_10", "فلينكوت", 8.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i24_11", "قباغ المنهول", 1.0, "عدد", FromMaterial("mat_33")),
                AnalysisLineSpec("i24_12", "الدفن بالتراب", 8.0, "م3", FromWorkItem("item_2")),
                AnalysisLineSpec("i24_13", "الرش", 1.0, "عدد", Fixed(1000.0))
            )
        ),

        // 25أ. سيرفيس سمك 4سم (مقاول)
        WorkItemSpec(
            id = "item_25_contractor",
            summaryOrder = 25,
            displayNumber = "25أ",
            title = "سيرفيس سمك 4سم (مقاول)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITH_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i25c_1", "مواد الانشائية", 1.0, "م2", Fixed(800.0)),
                AnalysisLineSpec("i25c_2", "اعمال الفرش", 1.0, "م2", Fixed(1500.0)),
                AnalysisLineSpec("i25c_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(400.0)),
                AnalysisLineSpec("i25c_4", "القير السيالي", 1.0, "م2", Fixed(2500.0)),
                AnalysisLineSpec("i25c_5", "برايم كوت", 1.0, "م2", Fixed(750.0)),
                AnalysisLineSpec("i25c_6", "الوقود", 1.0, "م2", Fixed(1750.0))
            )
        ),

        // 25ب. سيرفيس سمك 4سم (تنفيذ مباشر معمل بلدية)
        WorkItemSpec(
            id = "item_25_direct",
            summaryOrder = 26,
            displayNumber = "25ب",
            title = "سيرفيس سمك 4سم (تنفيذ مباشر معمل بلدية)",
            summaryTitle = "سيرفيس سمك 4سم (تنفيذ مباشر)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITHOUT_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i25d_1", "مواد الانشائية", 1.0, "م2", Fixed(800.0)),
                AnalysisLineSpec("i25d_2", "اعمال الفرش", 1.0, "م2", Fixed(0.0)),
                AnalysisLineSpec("i25d_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(400.0)),
                AnalysisLineSpec("i25d_4", "عمال المعمل وفرقة الفرش", 1.0, "م2", Fixed(0.0)),
                AnalysisLineSpec("i25d_5", "القير السيالي", 1.0, "م2", Fixed(2500.0)),
                AnalysisLineSpec("i25d_6", "برايم كوت", 1.0, "م2", Fixed(750.0)),
                AnalysisLineSpec("i25d_7", "الوقود", 1.0, "م2", Fixed(1750.0))
            )
        ),

        // 26. بايندر سمك 6سم (تنفيذ مباشر معمل بلدية)
        WorkItemSpec(
            id = "item_26",
            summaryOrder = 27,
            displayNumber = "26",
            title = "بايندر سمك 6سم (تنفيذ مباشر معمل بلدية)",
            summaryTitle = "بايندر سمك 6سم (تنفيذ مباشر)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITHOUT_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i26_1", "مواد الانشائية", 1.0, "م2", Fixed(1400.0)),
                AnalysisLineSpec("i26_2", "اعمال الفرش", 1.0, "م2", Fixed(0.0)),
                AnalysisLineSpec("i26_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(667.0)),
                AnalysisLineSpec("i26_4", "القير السيالي", 1.0, "م2", Fixed(3950.0)),
                AnalysisLineSpec("i26_5", "برايم كوت", 1.0, "م2", Fixed(725.0)),
                AnalysisLineSpec("i26_6", "الوقود", 1.0, "م2", Fixed(2750.0))
            )
        ),

        // 27. بايندر سمك 6سم (مقاول)
        WorkItemSpec(
            id = "item_27",
            summaryOrder = 28,
            displayNumber = "27",
            title = "بايندر سمك 6سم (مقاول)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITH_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i27_1", "مواد الانشائية", 1.0, "م2", Fixed(1400.0)),
                AnalysisLineSpec("i27_2", "اعمال الفرش", 1.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i27_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(667.0)),
                AnalysisLineSpec("i27_4", "القير السيالي", 1.0, "م2", Fixed(3950.0)),
                AnalysisLineSpec("i27_5", "برايم كوت", 1.0, "م2", Fixed(725.0)),
                AnalysisLineSpec("i27_6", "الوقود", 1.0, "م2", Fixed(2750.0))
            )
        )
    )
}
