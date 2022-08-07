package eco.point.domain.models.enums

enum class UserKeys(val key: String){
    ID("id"),
    STORAGE_NAME("my_storage"),
    NAME("Name"),
    CITY("City"),
    STATUS("UserStatus"),
    VISITED("is_visited"),
    EDUCATED("is_educated"),
    SKIP("Skiped"),
    BONUSES("Bonuses"),
    LEVEL("Level"),
    RATING("Rating"),
    GARBAGE_COUNTER("GarbageHandOverCount"),
    BIRTH("BirthDate"),
    BANNED("Banned")
}
