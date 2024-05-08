package Data

data class MainData(
    var goalNutritionInfo : GoalNutritionInfo,
    var historyInfoList : Map<String,HistoryInfo>
)
