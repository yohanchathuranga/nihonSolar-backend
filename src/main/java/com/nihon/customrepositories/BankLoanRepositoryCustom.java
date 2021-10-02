/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOBankLoan;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface BankLoanRepositoryCustom {

    List<DOBankLoan> listBankLoans(String query);

    Object countBankLoans(String query);
}
