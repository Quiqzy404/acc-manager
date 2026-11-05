package dev.quiqzy.accsmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AccountManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("accs-manager/accounts.json");

    private static final List<AccountProfile> ACCOUNTS = new ArrayList<>();

    private AccountManager() {}

    public static synchronized void load() {
        try {
            Files.createDirectories(FILE.getParent());

            if (!Files.exists(FILE)) {
                return;
            }

            Type type = new TypeToken<List<AccountProfile>>() {}.getType();

            List<AccountProfile> loaded =
                    GSON.fromJson(Files.readString(FILE), type);

            ACCOUNTS.clear();

            if (loaded != null) {
                ACCOUNTS.addAll(loaded);
            }

        } catch (Exception e) {
            System.err.println("Could not load accounts.");
        }
    }

    public static synchronized void add(AccountProfile account) {
        ACCOUNTS.removeIf(existing ->
                existing.uuid().equals(account.uuid()));

        ACCOUNTS.add(account);
        save();
    }

    public static synchronized void remove(String uuid) {
        ACCOUNTS.removeIf(account ->
                account.uuid().equals(uuid));

        save();
    }

    public static synchronized List<AccountProfile> getAccounts() {
        return Collections.unmodifiableList(new ArrayList<>(ACCOUNTS));
    }

    public static synchronized void save() {
        try {
            Files.createDirectories(FILE.getParent());

            Path temp = FILE.resolveSibling("accounts.tmp");

            Files.writeString(temp, GSON.toJson(ACCOUNTS));

            try {
                Files.move(temp, FILE,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(temp, FILE,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception e) {
            System.err.println("Could not save accounts.");
        }
    }
}
