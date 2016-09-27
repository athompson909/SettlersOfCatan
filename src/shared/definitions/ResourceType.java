package shared.definitions;

import com.google.gson.annotations.SerializedName;

public enum ResourceType
{
	@SerializedName("wood")
	WOOD,

	@SerializedName("brick")
	BRICK,

	@SerializedName("sheep")
	SHEEP,

	@SerializedName("wheat")
	WHEAT,

	@SerializedName("ore")
	ORE
}

