package com.pchome.akbadm.factory.notice;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeEnableReminded extends ANotice {
    private long LIMIT_OF_ACTIVATE = 3 * 24 * 60 * 60 * 1000;
    private EnumCategory enumCategory = EnumCategory.ENABLE_REMINDED;
    private EnumBoardType enumBoardType = EnumBoardType.AD;

    @Override
    public void notice() {
        if (!enumCategory.isBoard() && !enumCategory.isMailbox()) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Date activateDate = null;
        boolean hasAd = false;
        List<PfpCustomerInfo> list = pfpCustomerInfoService.selectCustomerInfo(EnumAccountStatus.START);
        for (PfpCustomerInfo pfpCustomerInfo: list) {
//            log.info("customerInfoId = " + pfpCustomerInfo.getCustomerInfoId());

            hasAd = false;

            try {
                // business logic
                activateDate = pfpCustomerInfo.getActivateDate();
                if (activateDate == null) {
//                    log.info("activateDate = null");
                    continue;
                }
                if (activateDate.getTime() + LIMIT_OF_ACTIVATE > calendar.getTimeInMillis()) {
//                    log.info("activateDate " + activateDate + " " + calendar.getTime());
                    continue;
                }

                for (PfpAdAction pfpAdAction: pfpCustomerInfo.getPfpAdActions()) {
                    if (EnumAdStatus.Open.getStatusId() != pfpAdAction.getAdActionStatus()) {
                        continue;
                    }

                    endCalendar.setTime(pfpAdAction.getAdActionEndDate());
                    endCalendar.add(Calendar.DAY_OF_YEAR, 1);
                    if (endCalendar.getTimeInMillis() < calendar.getTimeInMillis()) {
                        continue;
                    }

                    for (PfpAdGroup pfpAdGroup: pfpAdAction.getPfpAdGroups()) {
                        if (EnumAdStatus.Open.getStatusId() != pfpAdGroup.getAdGroupStatus()) {
                            continue;
                        }

                        for (PfpAd pfpAd: pfpAdGroup.getPfpAds()) {
                            if (EnumAdStatus.Open.getStatusId() != pfpAd.getAdStatus()) {
                                continue;
                            }

                            boardProvider.delete(pfpCustomerInfo.getCustomerInfoId(), enumCategory);
                            hasAd = true;
                            break;
                        }

                        if (hasAd) {
                            break;
                        }
                    }

                    if (hasAd) {
                        break;
                    }
                }

                if (hasAd) {
                    continue;
                }

                // board
                addBoard(pfpCustomerInfo, enumBoardType, enumCategory);

                // mailbox
                addMailbox(pfpCustomerInfo, enumCategory);
            } catch (Exception e) {
                log.error(pfpCustomerInfo.getCustomerInfoId(), e);
            }
        }

        log.info("board " + boardCount);
        log.info("mailbox " + mailboxCount);
    }

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }
}