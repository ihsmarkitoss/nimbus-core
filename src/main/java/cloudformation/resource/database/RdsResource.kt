package cloudformation.resource.database

import cloudformation.persisted.NimbusState
import cloudformation.resource.Resource
import cloudformation.resource.ec2.SecurityGroupResource
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class RdsResource(
        private val databaseConfiguration: DatabaseConfiguration,
        private val securityGroup: SecurityGroupResource,
        private val subnetGroup: SubnetGroup,
        nimbusState: NimbusState
): Resource(nimbusState) {

    init {
        addDependsOn(securityGroup)
        addDependsOn(subnetGroup)
    }

    override fun toCloudFormation(): JsonObject {
        val dbInstance = JsonObject()
        dbInstance.addProperty("Type", "AWS::RDS::DBInstance")

        val properties = getProperties()
        properties.addProperty("AllocatedStorage", "20")
        properties.addProperty("DBInstanceIdentifier", databaseConfiguration.name)
        properties.addProperty("DBInstanceClass", "db.t2.micro")
        properties.addProperty("Engine", "mysql")
        properties.addProperty("PubliclyAccessible", true)
        properties.addProperty("MasterUsername", databaseConfiguration.username)
        properties.addProperty("MasterUserPassword", databaseConfiguration.password)

        val securityGroups = JsonArray()
        securityGroups.add(securityGroup.getRef())

        properties.add("VPCSecurityGroups", securityGroups)

        properties.add("DBSubnetGroupName", subnetGroup.getRef())

        dbInstance.add("Properties", properties)

        dbInstance.add("DependsOn", dependsOn)

        return dbInstance
    }

    override fun getName(): String {
        return "${databaseConfiguration.name}RdsInstance"
    }
}