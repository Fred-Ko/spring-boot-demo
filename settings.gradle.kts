rootProject.name = "restaurant-msa-project"

// customer 모듈
include(
    "domains:customer:core",
    "domains:customer:adapter",
    "domains:customer:application"
)

// outbox 모듈
include("outbox")
