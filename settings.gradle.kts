rootProject.name = "restaurant-msa-project"

// customer 모듈
include(
    "domains:customer:core",
    "domains:customer:adapter",
    "domains:customer:application"
)

// outbox 모듈
include(
    "independent:outbox:core",
    "independent:outbox:adapter"
)
                    // order 모듈
                    include("order")
include("order:adapter")
include("order:core")
include("order:shared")
include("order:test")
