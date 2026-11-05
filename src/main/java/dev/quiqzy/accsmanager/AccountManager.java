
package dev.quiqzy.accsmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AccountManager {

    private static final List<AccountProfile> ACCOUNTS =
            new ArrayList<>();

    private AccountManager() {
    }

    public static void add(AccountProfile account) {
        if (account == null) {
            return;
        }

        ACCOUNTS.removeIf(existing ->
                existing.uuid().equals(account.uuid())
        );

        ACCOUNTS.add(account);
    }

    public static List<AccountProfile> getAccounts() {
        return Collections.unmodifiableList(ACCOUNTS);
    }

    public static void remove(String uuid) {
        ACCOUNTS.removeIf(account ->
                account.uuid().equals(uuid)
        );
    }

    public static void clear() {
        ACCOUNTS.clear();
    }
}
