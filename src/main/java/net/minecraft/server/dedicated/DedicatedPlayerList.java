package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

public class DedicatedPlayerList extends PlayerList {

    private static final Logger field_164439_d = LogManager.getLogger();

    public DedicatedPlayerList(DedicatedServer dedicatedserver) {
        super(dedicatedserver);
        this.func_152611_a(dedicatedserver.func_71327_a("view-distance", 10));
        this.field_72405_c = dedicatedserver.func_71327_a("max-players", 20);
        this.func_72371_a(dedicatedserver.func_71332_a("white-list", false));
        if (!dedicatedserver.func_71264_H()) {
            this.func_152608_h().func_152686_a(true);
            this.func_72363_f().func_152686_a(true);
        }

        this.func_187246_z();
        this.func_187248_x();
        this.func_187249_y();
        this.func_187247_w();
        this.func_72417_t();
        this.func_72418_v();
        this.func_72419_u();
        if (!this.func_152599_k().func_152691_c().exists()) {
            this.func_72421_w();
        }

    }

    @Override
    public void func_72371_a(boolean flag) {
        super.func_72371_a(flag);
        this.func_72365_p().func_71328_a("white-list", Boolean.valueOf(flag));
        this.func_72365_p().func_71326_a();
    }

    @Override
    public void func_152605_a(GameProfile gameprofile) {
        super.func_152605_a(gameprofile);
        this.func_72419_u();
    }

    @Override
    public void func_152610_b(GameProfile gameprofile) {
        super.func_152610_b(gameprofile);
        this.func_72419_u();
    }

    @Override
    public void func_152597_c(GameProfile gameprofile) {
        super.func_152597_c(gameprofile);
        this.func_72421_w();
    }

    @Override
    public void func_152601_d(GameProfile gameprofile) {
        super.func_152601_d(gameprofile);
        this.func_72421_w();
    }

    @Override
    public void func_187244_a() {
        this.func_72418_v();
    }

    private void func_187247_w() {
        try {
            this.func_72363_f().func_152678_f();
        } catch (IOException ioexception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to save ip banlist: ", ioexception);
        }

    }

    private void func_187248_x() {
        try {
            this.func_152608_h().func_152678_f();
        } catch (IOException ioexception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to save user banlist: ", ioexception);
        }

    }

    private void func_187249_y() {
        try {
            this.func_72363_f().func_152679_g();
        } catch (IOException ioexception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to load ip banlist: ", ioexception);
        }

    }

    private void func_187246_z() {
        try {
            this.func_152608_h().func_152679_g();
        } catch (IOException ioexception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to load user banlist: ", ioexception);
        }

    }

    private void func_72417_t() {
        try {
            this.func_152603_m().func_152679_g();
        } catch (Exception exception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to load operators list: ", exception);
        }

    }

    private void func_72419_u() {
        try {
            this.func_152603_m().func_152678_f();
        } catch (Exception exception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to save operators list: ", exception);
        }

    }

    private void func_72418_v() {
        try {
            this.func_152599_k().func_152679_g();
        } catch (Exception exception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to load white-list: ", exception);
        }

    }

    private void func_72421_w() {
        try {
            this.func_152599_k().func_152678_f();
        } catch (Exception exception) {
            DedicatedPlayerList.field_164439_d.warn("Failed to save white-list: ", exception);
        }

    }

    @Override
    public boolean func_152607_e(GameProfile gameprofile) {
        return !this.func_72383_n() || this.func_152596_g(gameprofile) || this.func_152599_k().func_152705_a(gameprofile);
    }

    @Override
    public DedicatedServer func_72365_p() {
        return (DedicatedServer) super.func_72365_p();
    }

    @Override
    public boolean func_183023_f(GameProfile gameprofile) {
        return this.func_152603_m().func_183026_b(gameprofile);
    }

    public MinecraftServer getServer() {
        return this.getServer();
    }
}
