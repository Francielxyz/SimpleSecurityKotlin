//package simple.security.kotlin.config
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.context.event.ApplicationReadyEvent
//import org.springframework.context.event.EventListener
//import org.springframework.core.io.ClassPathResource
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
//import org.springframework.stereotype.Component
//import javax.sql.DataSource
//
//@Component
//class InitializeData {
//
//    @Autowired
//    private lateinit var dataSource: DataSource
//
//    @EventListener(ApplicationReadyEvent::class)
//    fun loadData() =
//        ResourceDatabasePopulator(
//            true,
//            false,
//            "UTF-8",
//            ClassPathResource("/db/insert_classrrom.sql")
//        ).execute(dataSource)
//}