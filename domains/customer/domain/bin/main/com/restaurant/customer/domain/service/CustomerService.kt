import com.example.customer.domain.Customer

interface ICustomerService {
    fun createCustomer(customer: Customer): Customer
    fun getCustomerById(id: Long): Customer?
}