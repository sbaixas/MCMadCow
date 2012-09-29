package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityMadCow extends EntityAnimal
{
	public String name="Mad Cow";
	/** Above zero if this PigZombie is Angry. */
	private int angerLevel = 0;
	private int randomSoundDelay=0;
	public EntityMadCow(World par1World)
	{
		super(par1World);
		this.texture = "/mob/madcow.png";
		this.moveSpeed = 0.4F;
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIBreakDoor(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, this.moveSpeed, true));
		this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
		this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, this.moveSpeed, false));
		this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
	}

	public int getMaxHealth()
	{
		return 10;
	}

	/**
	 * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue()
	{
		return 2;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled()
	{
		if(angerLevel==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.cow";
	}
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("Anger", (short)this.angerLevel);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.angerLevel = par1NBTTagCompound.getShort("Anger");
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere()
	{
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID && this.worldObj.getFullBlockLightValue(var1, var2, var3) > 8 && super.getCanSpawnHere();
	}
	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.cowhurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.cowhurt";
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	protected int getDropItemId()
	{
		return Item.beefRaw.shiftedIndex;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}


	protected void dropRareDrop(int par1)
	{
		switch (this.rand.nextInt(4))
		{
		case 0:
			this.dropItem(Item.swordSteel.shiftedIndex, 1);
			break;

		case 1:
			this.dropItem(Item.helmetSteel.shiftedIndex, 1);
			break;

		case 2:
			this.dropItem(Item.ingotIron.shiftedIndex, 1);
			break;

		case 3:
			this.dropItem(Item.shovelSteel.shiftedIndex, 1);
		}
	}
	protected Entity findPlayerToAttack()
	{
		return this.angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	public boolean attackEntityAsMob(Entity par1Entity)
	{
		int var2 = 3;

		if (this.isPotionActive(Potion.damageBoost))
		{
			var2 += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
		}

		if (this.isPotionActive(Potion.weakness))
		{
			var2 -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
		}

		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
	}
	public boolean attackEntityFrom1(DamageSource par1DamageSource, int par2)
	{
		if (super.attackEntityFrom(par1DamageSource, par2))
		{
			Entity var3 = par1DamageSource.getEntity();

			if (this.riddenByEntity != var3 && this.ridingEntity != var3)
			{
				if (var3 != this)
				{
					this.entityToAttack = var3;
				}

				return true;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	/**
	 * Called to update the entity's position/logic.
	 */
	 public void onUpdate()
	{
		this.moveSpeed = this.entityToAttack != null ? 0.95F : 0.5F;

		if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
		{
			this.worldObj.playSoundAtEntity(this, "mob.cowhurt", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		/**
		 * Called to update the entity's position/logic.
		 */
		super.onUpdate();

		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0)
		{
			this.setDead();
		}
	}
	 public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
	 {
		 Entity var3 = par1DamageSource.getEntity();

		 if (var3 instanceof EntityPlayer)
		 {
			 List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));
			 Iterator var5 = var4.iterator();

			 while (var5.hasNext())
			 {
				 Entity var6 = (Entity)var5.next();

				 if (var6 instanceof EntityMadCow)
				 {
					 EntityMadCow var7 = (EntityMadCow)var6;
					 var7.becomeAngryAt(var3);
				 }
			 }

			 this.becomeAngryAt(var3);
		 }

		 return attackEntityFrom1(par1DamageSource, par2);
	 }


	 private void becomeAngryAt(Entity par1Entity)
	 {
		 this.entityToAttack = par1Entity;
		 this.angerLevel = 400 + this.rand.nextInt(400);
		 this.randomSoundDelay = this.rand.nextInt(40);
	 }



	 @Override
	 public EntityAnimal spawnBabyAnimal(EntityAnimal var1) {
		 // TODO Auto-generated method stub
		 return null;
	 }
}
