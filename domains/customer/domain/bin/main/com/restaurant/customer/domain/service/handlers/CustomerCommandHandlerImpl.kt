package com.restaurant.customer.domain.service.handlers

import com.restaurant.customer.domain.service.commands.CreateCustomerCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerCommand
import com.restaurant.customer.domain.service.commands.DeleteCustomerCommand
import com.restaurant.customer.domain.service.commands.UpdateCustomerGradeCommand
import com.restaurant.customer.domain.service.commands.CreateCustomerInquiryCommand

class CustomerCommandHandlerImpl : CustomerCommandHandler {
    override fun handle(command: CreateCustomerCommand) {
        // 고객 생성 로직 구현
    }

    override fun handle(command: UpdateCustomerCommand) {
        // 고객 정보 업데이트 로직 구현
    }

    override fun handle(command: DeleteCustomerCommand) {
        // 고객 삭제 로직 구현
    }

    override fun handle(command: UpdateCustomerGradeCommand) {
        // 고객 등급 업데이트 로직 구현
    }

    override fun handle(command: CreateCustomerInquiryCommand) {
        // 고객 문의 생성 로직 구현
    }
} 