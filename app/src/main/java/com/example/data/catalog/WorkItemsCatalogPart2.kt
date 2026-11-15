package com.example.data.catalog

import com.example.data.model.AnalysisLineSpec
import com.example.data.model.ItemCategory
import com.example.data.model.LineFormula.Fixed
import com.example.data.model.LineFormula.FromMaterial
import com.example.data.model.LineFormula.FromWorkItem
import com.example.data.model.ProfitRule
import com.example.data.model.WorkItemSpec

object WorkItemsCatalogPart2 {
    val items: List<WorkItemSpec> = listOf(
        // 28. ستابلايزر بسمك 10 سم (تنفيذ مباشر معمل بلدية)
        WorkItemSpec(
            id = "item_28",
            summaryOrder = 29,
            displayNumber = "28",
            title = "ستابلايزر بسمك 10 سم (تنفيذ مباشر معمل بلدية)",
            summaryTitle = "ستابلايزر بسمك 10 سم (تنفيذ مباشر)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITHOUT_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i28_1", "مواد الانشائية", 1.0, "م2", Fixed(1766.0)),
                AnalysisLineSpec("i28_2", "اعمال الفرش", 1.0, "م2", Fixed(0.0)),
                AnalysisLineSpec("i28_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i28_4", "القير السيالي", 1.0, "م2", Fixed(5400.0)),
                AnalysisLineSpec("i28_5", "برايم كوت", 1.0, "م2", Fixed(750.0)),
                AnalysisLineSpec("i28_6", "الوقود", 1.0, "م2", Fixed(3900.0))
            )
        ),

        // 29. ستابلايزر بسمك 10 سم (مقاول)
        WorkItemSpec(
            id = "item_29",
            summaryOrder = 30,
            displayNumber = "29",
            title = "ستابلايزر بسمك 10 سم (مقاول)",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.ONLY_WITH_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i29_1", "مواد الانشائية", 1.0, "م2", Fixed(1766.0)),
                AnalysisLineSpec("i29_2", "اعمال الفرش", 1.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i29_3", "نقل المزيج الى المشروع", 1.0, "م2", Fixed(800.0)),
                AnalysisLineSpec("i29_4", "القير السيالي", 1.0, "م2", Fixed(5400.0)),
                AnalysisLineSpec("i29_5", "برايم كوت", 1.0, "م2", Fixed(750.0)),
                AnalysisLineSpec("i29_6", "الوقود", 1.0, "م2", Fixed(3900.0))
            )
        ),

        // 30. برايم كوت / م2
        WorkItemSpec(
            id = "item_30",
            summaryOrder = 31,
            displayNumber = "30",
            title = "برايم كوت / م2",
            unit = "م2",
            category = ItemCategory.ASPHALT,
            profitRule = ProfitRule.NO_PROFIT_SAME,
            lines = listOf(
                AnalysisLineSpec("i30_1", "برايم كوت / م2", 1.0, "م2", Fixed(750.0))
            )
        ),

        // 31. مجاري كونكريت مسلح بقطر 90سم
        WorkItemSpec(
            id = "item_31",
            summaryOrder = 32,
            displayNumber = "31",
            title = "مجاري كونكريت مسلح بقطر 90سم",
            unit = "م.ط",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i31_1", "حفريات", 3.0, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i31_2", "تجهيز بوري", 1.0, "م.ط", FromMaterial("mat_23")),
                AnalysisLineSpec("i31_3", "حجر مكسر + عمال", 0.15, "م.ط", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i31_4", "صب الاساس + صب حول المجاري", 1.2, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i31_5", "تنصيب وتركيب بوري", 1.0, "م.ط", Fixed(5000.0)),
                AnalysisLineSpec("i31_6", "درز", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i31_7", "رش", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i31_8", "دفن بالتراب", 0.6, "م3", FromWorkItem("item_2"))
            )
        ),

        // 32. المنهولات بابعاد (1.2*1.2)م
        WorkItemSpec(
            id = "item_32",
            summaryOrder = 33,
            displayNumber = "32",
            title = "المنهولات بابعاد (1.2*1.2)م",
            unit = "عدد",
            category = ItemCategory.SEWAGE_MANHOLES,
            lines = listOf(
                AnalysisLineSpec("i32_1", "حفريات", 11.6, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i32_2", "حجر مكسر + عمال", 0.4, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i32_3", "صب القاعدة", 1.5, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i32_4", "صب السقف", 0.8, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i32_5", "حديد تسليح", 40.0, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i32_6", "بلوك", 5.5, "م3", FromWorkItem("item_18")),
                AnalysisLineSpec("i32_7", "مجاري 90سم داخل المنهول", 0.8, "م.ط", FromMaterial("mat_23")),
                AnalysisLineSpec("i32_8", "حداد", 40.0, "كغم", FromMaterial("mat_28", divisor = 1000.0)),
                AnalysisLineSpec("i32_9", "نجار قالب خشبي", 10.0, "م2", Fixed(6500.0)),
                AnalysisLineSpec("i32_10", "لبخ", 11.0, "م2", FromWorkItem("item_19")),
                AnalysisLineSpec("i32_11", "فلينكوت", 11.0, "م2", Fixed(2000.0)),
                AnalysisLineSpec("i32_12", "قباغ المنهول قطر 60سم", 1.0, "عدد", FromMaterial("mat_33"))
            )
        ),

        // 33. حفريات اساس الجدار الساند
        WorkItemSpec(
            id = "item_33",
            summaryOrder = 34,
            displayNumber = "33",
            title = "حفريات اساس الجدار الساند",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i33_1", "حفريات", 1.0, "م3", FromWorkItem("item_3"), "الحفريات")
            )
        ),

        // 34. صب اساس الجدار الساند
        WorkItemSpec(
            id = "item_34",
            summaryOrder = 35,
            displayNumber = "34",
            title = "صب اساس الجدار الساند",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i34_1", "تجهيز وفرش وحدل طبقة الحجر مكسر بسمك 10سم", 0.35, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i34_2", "صبة الاساس", 1.0, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i34_3", "تجهيز الماء والرش", 1.0, "م3", Fixed(2000.0)),
                AnalysisLineSpec("i34_4", "الفحوصات", 1.0, "م3", Fixed(3000.0))
            )
        ),

        // 35. جدار ساند بالحجر العادي لب كونكريت مع استعمال الحجر المعدل للوجه
        WorkItemSpec(
            id = "item_35",
            summaryOrder = 36,
            displayNumber = "35",
            title = "جدار ساند بالحجر العادي لب كونكريت مع استعمال الحجر المعدل للوجه",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i35_1", "تجهيز حجر", 0.55, "م3", FromMaterial("mat_25", divisor = 8.0)),
                AnalysisLineSpec("i35_2", "تعديل الوجه للحجر بابعاد 30*30 سم", 11.0, "قطعة", Fixed(1000.0)),
                AnalysisLineSpec("i35_3", "درز الحجر المعدل", 1.0, "م3", Fixed(8000.0)),
                AnalysisLineSpec("i35_4", "بناء مع العمال", 0.55, "م3", FromMaterial("mat_26")),
                AnalysisLineSpec("i35_5", "صب (لب الجدار)", 0.45, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i35_6", "رمل", 0.2, "م3", FromMaterial("mat_11", divisor = 9.0)),
                AnalysisLineSpec("i35_7", "سمنت", 70.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i35_8", "فلتر (جلمود + p.v.c 4 انج)", 1.0, "م3", Fixed(6500.0)),
                AnalysisLineSpec("i35_9", "تجهيز ماء مع الرش", 1.0, "م3", Fixed(1000.0)),
                AnalysisLineSpec("i35_10", "فحوصات", 1.0, "جملة", Fixed(2500.0))
            )
        ),

        // 36. جدار ساند بالحجر العادي لب كونكريت
        WorkItemSpec(
            id = "item_36",
            summaryOrder = 37,
            displayNumber = "36",
            title = "جدار ساند بالحجر العادي لب كونكريت",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i36_1", "تجهيز حجر", 0.55, "م3", FromMaterial("mat_25", divisor = 8.0)),
                AnalysisLineSpec("i36_2", "بناء مع العمال", 0.55, "م3", FromMaterial("mat_26")),
                AnalysisLineSpec("i36_3", "صب (لب الجدار)", 0.45, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i36_4", "رمل", 0.2, "م3", FromMaterial("mat_11", divisor = 9.0)),
                AnalysisLineSpec("i36_5", "سمنت", 70.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i36_6", "فلتر (جلمود + p.v.c 4 انج)", 1.0, "م3", Fixed(6500.0)),
                AnalysisLineSpec("i36_7", "تجهيز ماء مع الرش", 1.0, "م3", Fixed(1000.0)),
                AnalysisLineSpec("i36_8", "فحوصات", 1.0, "جملة", Fixed(2500.0))
            )
        ),

        // 37. جدار ساند بالبلوك الكونكريتي الصلد
        WorkItemSpec(
            id = "item_37",
            summaryOrder = 38,
            displayNumber = "37",
            title = "جدار ساند بالبلوك الكونكريتي الصلد",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i37_1", "تجهيز البلوك", 80.0, "عدد", FromMaterial("mat_19")),
                AnalysisLineSpec("i37_2", "بناء مع العمال", 1.0, "م3", FromMaterial("mat_31")),
                AnalysisLineSpec("i37_3", "رمل", 0.15, "م3", FromMaterial("mat_11", divisor = 9.0)),
                AnalysisLineSpec("i37_4", "سمنت", 70.0, "كغم", FromMaterial("mat_12", divisor = 1000.0)),
                AnalysisLineSpec("i37_5", "فلتر (جلمود + p.v.c 4 انج)", 1.0, "م3", Fixed(6500.0)),
                AnalysisLineSpec("i37_6", "تجهيز ماء مع الرش", 1.0, "م3", Fixed(1000.0)),
                AnalysisLineSpec("i37_7", "فحوصات", 1.0, "جملة", Fixed(2500.0))
            )
        ),

        // 38. جدار ساند مسلح
        WorkItemSpec(
            id = "item_38",
            summaryOrder = 39,
            displayNumber = "38",
            title = "جدار ساند مسلح مع حفريات الاساس وصب الاساس",
            summaryTitle = "جدار ساند مسلح",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i38_1", "حفريات", 1.6, "م3", FromWorkItem("item_3")),
                AnalysisLineSpec("i38_2", "حجر مكسر + عمال", 1.0, "م3", FromMaterial("mat_9", divisor = 9.0, addend = 15_000.0)),
                AnalysisLineSpec("i38_3", "صب الأساس والجدار", 1.0, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i38_4", "قالب خشبي", 5.0, "م2", Fixed(6500.0)),
                AnalysisLineSpec("i38_5", "حديد تسليح", 160.0, "كغم", FromMaterial("mat_22", divisor = 1000.0)),
                AnalysisLineSpec("i38_6", "حداد", 160.0, "كغم", FromMaterial("mat_28", divisor = 1000.0)),
                AnalysisLineSpec("i38_7", "فلتر (جلمود + p.v.c 4 انج)", 1.0, "م3", Fixed(6500.0)),
                AnalysisLineSpec("i38_8", "تجهيز ماء مع الرش", 1.0, "م3", Fixed(1000.0)),
                AnalysisLineSpec("i38_9", "فحوصات", 1.0, "جملة", Fixed(10_000.0))
            )
        ),

        // 39. جدار ساند من الكابيون
        WorkItemSpec(
            id = "item_39",
            summaryOrder = 40,
            displayNumber = "39",
            title = "جدار ساند من الكابيون",
            unit = "م3",
            category = ItemCategory.BUILDINGS_WALLS,
            lines = listOf(
                AnalysisLineSpec("i39_1", "فرش طبقة من الجلمود للاساس", 0.1, "م3", FromMaterial("mat_37", divisor = 9.0)),
                AnalysisLineSpec("i39_2", "بي ار سي 5 ملم - فتحات 7*7سم", 4.25, "م2", FromMaterial("mat_35")),
                AnalysisLineSpec("i39_3", "بي ار سي 5 ملم - فتحات 5*5سم للواجه المعرضة للماء", 1.0, "م2", FromMaterial("mat_36")),
                AnalysisLineSpec("i39_4", "سبرنك مع التيل لعمل الصناديق", 8.0, "م.ط", Fixed(750.0)),
                AnalysisLineSpec("i39_5", "جلمود", 1.0, "م3", FromMaterial("mat_37", divisor = 9.0)),
                AnalysisLineSpec("i39_6", "حفارة", 1.0, "م3", Fixed(6000.0)),
                AnalysisLineSpec("i39_7", "عمال", 1.0, "م3", Fixed(3500.0))
            )
        ),

        // 40. تحويل وتنصيب أعمدة الكهرباء
        WorkItemSpec(
            id = "item_40",
            summaryOrder = 41,
            displayNumber = "40",
            title = "تحويل وتنصيب أعمدة الكهرباء بمختلف الفولطيات مع الشبكة وإجراء التحويرات اللازمة.",
            unit = "عدد",
            category = ItemCategory.UTILITIES_PARKS,
            profitRule = ProfitRule.ONLY_WITHOUT_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i40_1", "حسب الكشف المسعر من مديرية الكهرباء", 1.0, "عمود", Fixed(500_000.0))
            )
        ),

        // 41. تجهيز وتركيب القفيس الكهربائي لشبكة المياه
        WorkItemSpec(
            id = "item_41",
            summaryOrder = 42,
            displayNumber = "41",
            title = "تجهيز وتركيب القفيس الكهربائي لشبكة المياه.",
            unit = "عدد",
            category = ItemCategory.UTILITIES_PARKS,
            profitRule = ProfitRule.ONLY_WITHOUT_PROFIT,
            lines = listOf(
                AnalysisLineSpec("i41_1", "تجهيز وتركيب القفيس الكهربائي", 1.0, "اشتراك", Fixed(70_000.0))
            )
        ),

        // 42. صبغ الجدران
        WorkItemSpec(
            id = "item_42",
            summaryOrder = 43,
            displayNumber = "42",
            title = "صبغ الجدران",
            unit = "م2",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i42_1", "تجهيز الصبغ", 1.0, "م2", Fixed(1500.0), "صبغ الجدران"),
                AnalysisLineSpec("i42_2", "عمل قاطين", 1.0, "م2", Fixed(1000.0))
            )
        ),

        // 43. لاحات الازهار
        WorkItemSpec(
            id = "item_43",
            summaryOrder = 44,
            displayNumber = "43",
            title = "لاحات الازهار",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i43_1", "تجهيز", 1.0, "م.ط", Fixed(3250.0)),
                AnalysisLineSpec("i43_2", "بناء مع العمال", 1.0, "م.ط", Fixed(1250.0))
            )
        ),

        // 44. سياج BRC
        WorkItemSpec(
            id = "item_44",
            summaryOrder = 45,
            displayNumber = "44",
            title = "سياج BRC بارتفاع 1.2م وقطر الشبكة (4.5-5)ملم، وكل 2م اعمدة",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i44_1", "تجهيز", 1.0, "م.ط", Fixed(20_000.0), "سياج BRC بارتفاع 1.2م"),
                AnalysisLineSpec("i44_2", "حمل ونقل", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i44_3", "نصب وتثبيت مع العمال وصب القواعد", 1.0, "م.ط", Fixed(5000.0))
            )
        ),

        // 45. الابواب BRC
        WorkItemSpec(
            id = "item_45",
            summaryOrder = 46,
            displayNumber = "45",
            title = "الابواب BRC بعرض 1.2م و ارتفاع 1.2م مع الكيلون",
            unit = "عدد",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i45_1", "تجهيز", 1.0, "عدد", Fixed(120_000.0), "الابواب BRC بعرض 2م"),
                AnalysisLineSpec("i45_2", "كيلون", 1.0, "عدد", Fixed(10_000.0)),
                AnalysisLineSpec("i45_3", "نقل وتحميل", 1.0, "عدد", Fixed(15_000.0)),
                AnalysisLineSpec("i45_4", "تثبيت ونصب", 1.0, "عدد", Fixed(35_000.0))
            )
        ),

        // 46. الطابوق المعشق (6*15*20)سم
        WorkItemSpec(
            id = "item_46",
            summaryOrder = 47,
            displayNumber = "46",
            title = "الطابوق المعشق (6*15*20)سم",
            unit = "م2",
            category = ItemCategory.CONCRETE_KERBS,
            lines = listOf(
                AnalysisLineSpec("i46_1", "تجهيز طابوق", 1.0, "م2", Fixed(8000.0)),
                AnalysisLineSpec("i46_2", "الحمل والنقل", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i46_3", "تجهيز وفرش تيكلة سمك 15سم", 1.0, "م2", Fixed(4000.0)),
                AnalysisLineSpec("i46_4", "تجهيز رمل تحت الطابوق سمك 5سم", 1.0, "م2", Fixed(2250.0)),
                AnalysisLineSpec("i46_5", "مونة الصبة لقفل الجوانب", 1.0, "م2", Fixed(1250.0)),
                AnalysisLineSpec("i46_6", "ماء ورش تيكلة والطابوق", 1.0, "م2", Fixed(1000.0)),
                AnalysisLineSpec("i46_7", "اعمال رصف الطابوق مع العمال", 1.0, "م2", Fixed(2500.0)),
                AnalysisLineSpec("i46_8", "تجهيز رمل درز الفراغات الطابوق", 1.0, "م2", Fixed(500.0)),
                AnalysisLineSpec("i46_9", "فحوصات", 1.0, "جملة", Fixed(500.0))
            )
        ),

        // 47. تجهيز كابينات
        WorkItemSpec(
            id = "item_47",
            summaryOrder = 48,
            displayNumber = "47",
            title = "تجهيز كابينات",
            unit = "م2",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i47_1", "شراء كابينة", 1.0, "م2", Fixed(265_000.0)),
                AnalysisLineSpec("i47_2", "نقل وحمل وتثبيت", 1.0, "م2", Fixed(25_000.0))
            )
        ),

        // 48. خزان الماء بلاستيكي (10000) ليتر وصب القاعدة
        WorkItemSpec(
            id = "item_48",
            summaryOrder = 49,
            displayNumber = "48",
            title = "خزان الماء بلاستيكي (10000) ليتر و صب القاعدة",
            unit = "عدد",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i48_1", "شراء", 1.0, "عدد", Fixed(1_450_000.0)),
                AnalysisLineSpec("i48_2", "النقل والحمل", 1.0, "عدد", Fixed(50_000.0)),
                AnalysisLineSpec("i48_3", "تهيئة الموقع وتجهيز حجر وفرش حجر", 1.0, "جملة", Fixed(80_000.0)),
                AnalysisLineSpec("i48_4", "حدل ورش تحت الصبة", 1.0, "جملة", Fixed(45_000.0)),
                AnalysisLineSpec("i48_5", "صب القاعدة", 1.6, "م3", FromMaterial("mat_14")),
                AnalysisLineSpec("i48_6", "بورد الكهربائي", 1.0, "عدد", Fixed(50_000.0)),
                AnalysisLineSpec("i48_7", "ميزانية الكتروني كهربائي", 1.0, "عدد", Fixed(40_000.0)),
                AnalysisLineSpec("i48_8", "كيبل كهربائي", 15.0, "م", Fixed(8_000.0)),
                AnalysisLineSpec("i48_9", "ربط بورد وميزانية ومد كيبل", 1.0, "جملة", Fixed(198_000.0)),
                AnalysisLineSpec("i48_10", "تجهيز ماء مع الرش", 1.0, "عدد", Fixed(25_000.0)),
                AnalysisLineSpec("i48_11", "الملحقات (بوري، قفل، نبل)", 1.0, "عدد", Fixed(30_000.0)),
                AnalysisLineSpec("i48_12", "تجهيز مضخة ماء 1.5 حصان مع الربط", 1.0, "عدد", Fixed(200_000.0)),
                AnalysisLineSpec("i48_13", "عمل ربط الخزان بالمصدر", 1.0, "عدد", Fixed(100_000.0))
            )
        ),

        // 49. رمل الحدائق (زميج)
        WorkItemSpec(
            id = "item_49",
            summaryOrder = 50,
            displayNumber = "49",
            title = "رمل الحدائق (زميج)",
            unit = "م2",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i49_1", "شراء ونقل", 1.0, "م2", FromMaterial("mat_27", divisor = 70.0)),
                AnalysisLineSpec("i49_2", "فرش وتوزيع بالعمال", 1.0, "م2", Fixed(500.0))
            )
        ),

        // 50. انابيب التنقيط
        WorkItemSpec(
            id = "item_50",
            summaryOrder = 51,
            displayNumber = "50",
            title = "انابيب التنقيط",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i50_1", "تجهيز انبوب التنقيط مع القفل والصندوق", 1.0, "م.ط", Fixed(1000.0)),
                AnalysisLineSpec("i50_2", "نوزل", 1.0, "م.ط", Fixed(250.0)),
                AnalysisLineSpec("i50_3", "ربط مع العمال", 1.0, "م.ط", Fixed(500.0))
            )
        ),

        // 51. شبكة ماء (انابيب 1 انج) وحنفي
        WorkItemSpec(
            id = "item_51",
            summaryOrder = 52,
            displayNumber = "51",
            title = "شبكة ماء (انابيب 1 انج) وحنفي",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i51_1", "تجهيز", 1.0, "م.ط", Fixed(2000.0)),
                AnalysisLineSpec("i51_2", "تجهيز صمول", 1.0, "م.ط", Fixed(125.0, fixedAmountOverride = 150.0)),
                AnalysisLineSpec("i51_3", "تجهيز (حنفي، عكس)", 1.0, "م.ط", Fixed(450.0)),
                AnalysisLineSpec("i51_4", "حفريات ودفن مسار الانابيب", 1.0, "م.ط", Fixed(1500.0)),
                AnalysisLineSpec("i51_5", "ربط الحنفي مع العمال", 1.0, "م.ط", Fixed(400.0)),
                AnalysisLineSpec("i51_6", "تأسيس الانابيب مع العمال", 1.0, "م.ط", Fixed(1500.0))
            )
        ),

        // 52. ربط المصدر (خط الرئيسي)
        WorkItemSpec(
            id = "item_52",
            summaryOrder = 53,
            displayNumber = "52",
            title = "ربط المصدر (خط الرئيسي)",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i52_1", "قطع الشارع (كتر)", 1.0, "جملة", Fixed(75_000.0)),
                AnalysisLineSpec("i52_2", "اعادة معالجة الشارع بالصبة", 1.0, "جملة", Fixed(10_000.0)),
                AnalysisLineSpec("i52_3", "تجهيز قفيص", 1.0, "عدد", Fixed(40_000.0)),
                AnalysisLineSpec("i52_4", "تجهيز قفل مغلون", 1.0, "عدد", Fixed(2_000.0)),
                AnalysisLineSpec("i52_5", "بوري 1 انج", 10.0, "م.ط", Fixed(4_000.0)),
                AnalysisLineSpec("i52_6", "تجهيز نبل وصمول سن خارجي", 1.0, "جملة", Fixed(10_000.0)),
                AnalysisLineSpec("i52_7", "عمل حفر موقع المصدر ومسار البوري", 1.0, "جملة", Fixed(50_000.0)),
                AnalysisLineSpec("i52_8", "عمل ربط المصدر", 1.0, "جملة", Fixed(33_000.0))
            )
        ),

        // 53. رش اوتوماتيك
        WorkItemSpec(
            id = "item_53",
            summaryOrder = 54,
            displayNumber = "53",
            title = "رش اوتوماتيك",
            unit = "م2",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i53_1", "تجهيز بوري", 1.0, "م2", Fixed(3500.0)),
                AnalysisLineSpec("i53_2", "ملحقات (توصيل)", 1.0, "م2", Fixed(250.0)),
                AnalysisLineSpec("i53_3", "مرشة (فسكي)", 1.0, "م2", Fixed(250.0)),
                AnalysisLineSpec("i53_4", "الربط وحفريات والدفن مسار بوري", 1.0, "م3", Fixed(2000.0))
            )
        ),

        // 54. انبوب (بولي اثيلين) قياس 63ملم
        WorkItemSpec(
            id = "item_54",
            summaryOrder = 55,
            displayNumber = "54",
            title = "انبوب (بولي اثيلين) قياس 63ملم",
            unit = "م.ط",
            category = ItemCategory.UTILITIES_PARKS,
            lines = listOf(
                AnalysisLineSpec("i54_1", "تجهيز انبوب", 1.0, "م.ط", Fixed(2500.0, fixedAmountOverride = 3000.0)),
                AnalysisLineSpec("i54_2", "ملحقات (قفل، قفيص، نبل، حنفي)", 1.0, "م.ط", Fixed(500.0, fixedAmountOverride = 750.0)),
                AnalysisLineSpec("i54_3", "الحفريات والدفن مسار الانبوب", 1.0, "م.ط", Fixed(1250.0, fixedAmountOverride = 1500.0)),
                AnalysisLineSpec("i54_4", "الربط وحفريات والدفن مسار بوري", 1.0, "م.ط", Fixed(1450.0, fixedAmountOverride = 2000.0))
            )
        )
    )
}
