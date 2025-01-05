import com.restaurant.customer.domain.repository.entities.Customer

interface ICustomerService {
    fun createCustomer(customer: Customer): Customer
    fun getCustomerById(id: Long): Customer?
}