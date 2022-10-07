package eco.point.domain.models.enums

enum class ExchangeKeys(val key: String) {
    ID("id"),
    ANNOUNCEMENT_ID("AnnouncementId"),
    TITLE("title"),
    DATE_TIME("dateTime"),
    CREATOR_NAME("name"),
    CREATOR("creator"),
    CREATOR_ID("CreatorId"),
    PARTICIPANT("participant"),
    DESCRIPTION("description"),
    TAG("tag"),
    EMAIL("email"),
    VK_LINK("vkLink"),
    TG_LINK("tgLink"),
    TEL("telephone"),
    COMPLAINTS("complaint"),
    BANNED("banned"),
    COST("cost"),
    UNITS("units"),
    CITY("city"),
    //todo: delete tags
    PLASTIC("plastic"),
    GLASS("glass"),
    METAL("metal"),
    PAPER("paper"),
    FOOD("food"),
    BATTERY("battery")
}