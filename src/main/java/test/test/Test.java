package test.test;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Jigsaw;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Test extends JavaPlugin implements Listener {

    File dataFile = new File(getDataFolder(), "data.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Override
    public void onEnable() {

    Bukkit.getPluginManager().registerEvents(this, this);

    }
    @EventHandler
    public void OnAnvilPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (e.getBlock().getType() != Material.ANVIL) {
            if (e.getBlock().getType() != Material.CHIPPED_ANVIL) {
                if (e.getBlock().getType() != Material.DAMAGED_ANVIL) {
                    return;
                }
            }
        }

        if (!(e.getBlock().getBlockData() instanceof Directional)) {return;}

        Directional block2 = (Directional) e.getBlock().getBlockData();



        Location AnvilLocation = e.getBlock().getLocation();
        Location AnvilLocation2 = e.getBlock().getLocation();
        AnvilLocation2.setY(AnvilLocation2.getY()-0.8);
        AnvilLocation2.setX(AnvilLocation2.getX()+0.5);
        AnvilLocation2.setZ(AnvilLocation2.getZ()+0.5);
        ArmorStand armorStand = (ArmorStand) AnvilLocation.getWorld().spawnEntity(AnvilLocation2, EntityType.ARMOR_STAND);


        armorStand.setHelmet(new ItemStack(Material.AIR));
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);

        armorStand.getUniqueId();

        List<String> players = data.getStringList("players");
        String path = String.join(".", "anvils", AnvilLocation.toString(), ".ArmorStand");

        players.add(path);



        data.set("players", players);
        data.set(path, armorStand.getUniqueId().toString());

    }

    @EventHandler
    public void OnAnvil(PrepareAnvilEvent e) {


        Player player = (Player) e.getView().getPlayer();

        Location anvilLocation = e.getInventory().getLocation();

        ArmorStand AnvilArmorStand;

        try {
            String ArmorStandUUID = data.getString("anvils." + anvilLocation.toString() + ".ArmorStand");

            AnvilArmorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(ArmorStandUUID));
        } catch (Throwable t) {
            return;
        }

        if (AnvilArmorStand == null) {
            return;
        }



        float yaw = 0;
        float PlayerYaw = player.getLocation().getYaw();

        if (PlayerYaw > -181 && PlayerYaw < -91) {
            yaw = -180;
        }
        if (PlayerYaw > -91 && PlayerYaw < -1) {
            yaw = -90;
        }
        if (PlayerYaw > -1 && PlayerYaw < 91) {
            yaw = 0;
        }
        if (PlayerYaw > 91 && PlayerYaw < 180) {
            yaw = 90;
        }


        AnvilArmorStand.setRotation(yaw, 0);
        Location AnvilArmorStandLocation = AnvilArmorStand.getLocation();

        if ((e.getResult().getType() == Material.AIR)) {
            if (e.getInventory().getItem(1) == null) {
                if (e.getInventory().getItem(0) == null) {
                    AnvilArmorStand.setHelmet(new ItemStack(Material.AIR));
                    AnvilArmorStand.setCustomNameVisible(false);
                } else {
                    if (e.getInventory().getItem(0).getType() == Material.TRIDENT) {
                        AnvilArmorStandLocation.setY(anvilLocation.getY());
                        AnvilArmorStand.teleport(AnvilArmorStandLocation);
                    } else if (e.getInventory().getItem(0).getType() == Material.SHIELD) {
                        AnvilArmorStandLocation.setY(anvilLocation.getY());
                        AnvilArmorStand.teleport(AnvilArmorStandLocation);
                    } else {
                        AnvilArmorStandLocation.setY(anvilLocation.getY()-0.8);
                        AnvilArmorStand.teleport(AnvilArmorStandLocation);
                    }
                    AnvilArmorStand.setHelmet(e.getInventory().getItem(0));
                    AnvilArmorStand.setCustomNameVisible(true);
                    AnvilArmorStand.setCustomName(ChatColor.WHITE + e.getInventory().getItem(0).getItemMeta().getLocalizedName());
                }
            } else {
                if (e.getInventory().getItem(1).getType() == Material.TRIDENT) {
                    AnvilArmorStandLocation.setY(anvilLocation.getY());
                    AnvilArmorStand.teleport(AnvilArmorStandLocation);
                } else if (e.getInventory().getItem(1).getType() == Material.SHIELD) {
                    AnvilArmorStandLocation.setY(anvilLocation.getY());
                    AnvilArmorStand.teleport(AnvilArmorStandLocation);
                } else {
                    AnvilArmorStandLocation.setY(anvilLocation.getY()-0.8);
                    AnvilArmorStand.teleport(AnvilArmorStandLocation);
                }

                AnvilArmorStand.setHelmet(e.getInventory().getItem(1));
                AnvilArmorStand.setCustomNameVisible(true);
                AnvilArmorStand.setCustomName(ChatColor.LIGHT_PURPLE + e.getInventory().getItem(1).getItemMeta().getLocalizedName());
            }
        } else {
            if (e.getResult().getType() == Material.TRIDENT) {
                AnvilArmorStandLocation.setY(anvilLocation.getY());
                AnvilArmorStand.teleport(AnvilArmorStandLocation);
            } else if (e.getResult().getType() == Material.SHIELD) {
                AnvilArmorStandLocation.setY(anvilLocation.getY());
                AnvilArmorStand.teleport(AnvilArmorStandLocation);
            } else {
                AnvilArmorStandLocation.setY(anvilLocation.getY()-0.8);
                AnvilArmorStand.teleport(AnvilArmorStandLocation);
            }
            AnvilArmorStand.setHelmet(e.getResult());
            AnvilArmorStand.setCustomNameVisible(true);
            AnvilArmorStand.setCustomName(ChatColor.YELLOW + e.getResult().getItemMeta().getDisplayName());
        }


    }

    @EventHandler
    public void OnAnvilDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.ANVIL) {
            if (e.getBlock().getType() != Material.CHIPPED_ANVIL) {
                if (e.getBlock().getType() != Material.DAMAGED_ANVIL) {
                    return;
                }
            }
        }
        Location AnvilLocation = e.getBlock().getLocation();

        ArmorStand AnvilArmorStand;

        try {
            String ArmorStandUUID = data.getString("anvils." + AnvilLocation.toString() + ".ArmorStand");

            AnvilArmorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(ArmorStandUUID));
        } catch (Throwable t) {
            return;
        }

        if (AnvilArmorStand == null) {
            return;
        }

        AnvilArmorStand.remove();
    }

    @EventHandler
    public void OnAnvilInventoryView(InventoryCloseEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL) {
            return;
        }

        ArmorStand AnvilArmorStand;

        try {
            String ArmorStandUUID = data.getString("anvils." + e.getInventory().getLocation() + ".ArmorStand");

            AnvilArmorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(ArmorStandUUID));
        } catch (Throwable t) {
            return;
        }

        if (AnvilArmorStand == null) {
            return;
        }

        AnvilArmorStand.setHelmet(new ItemStack(Material.AIR));
        AnvilArmorStand.setCustomNameVisible(false);
    }

    @EventHandler
    public void onAnvilMove(BlockPistonExtendEvent e) {
        if (e.getBlock().getType() != Material.ANVIL) {
            if (e.getBlock().getType() != Material.CHIPPED_ANVIL) {
                if (e.getBlock().getType() != Material.DAMAGED_ANVIL) {
                    return;
                }
            }
        }

        ArmorStand AnvilArmorStand;

        try {
            String ArmorStandUUID = data.getString("anvils." + e.getBlock().getLocation() + ".ArmorStand");

            AnvilArmorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(ArmorStandUUID));
        } catch (Throwable t) {
            return;
        }

        if (AnvilArmorStand == null) {
            return;
        }

        AnvilArmorStand.remove();

    }

    @EventHandler
    public void OnAnvilFall(final EntityChangeBlockEvent e) {
        if (e.getBlock().getType() != Material.ANVIL) {
            if (e.getBlock().getType() != Material.CHIPPED_ANVIL) {
                if (e.getBlock().getType() != Material.DAMAGED_ANVIL) {
                    return;
                }
            }
        }

        ArmorStand AnvilArmorStand;

        try {
            String ArmorStandUUID = data.getString("anvils." + e.getBlock().getLocation() + ".ArmorStand");

            AnvilArmorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(ArmorStandUUID));
        } catch (Throwable t) {
            return;
        }

        if (AnvilArmorStand == null) {
            return;
        }

        AnvilArmorStand.remove();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

