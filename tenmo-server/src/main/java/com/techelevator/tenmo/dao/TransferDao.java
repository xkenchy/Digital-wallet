package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.postgresql.util.PSQLException;

import java.util.List;

public interface TransferDao {


    Transfer createTransfer(Transfer transfer, int fromId, int toId) throws PSQLException;

    List<Transfer> getAllTransfersByUserId (int userId);

    Transfer viewTransferDetails (int id);
}
