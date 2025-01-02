package com.restaurant.customer.domain.service.handlers

import com.restaurant.customer.domain.service.commands.CreateCustomerCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerCommand
import com.restaurant.customer.domain.service.commands.DeleteCustomerCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerGradeCommand
import com.restaurant.customer.domain.service.commands.CreateCustomerInquiryCommand

interface CustomerCommandHandler {
    fun handle(command: CreateCustomerCommand)
    fun handle(command: UpdateCustomerCommand)
    fun handle(command: DeleteCustomerCommand)
    fun handle(command: UpdateCustomerGradeCommand)
    fun handle(command: CreateCustomerInquiryCommand)
}
