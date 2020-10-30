package com.royalblueranger.mgpq.db;

/**
 * <h1>Trash - keep comments?</h1>
 * 
 * <p>This class will provide some of the functional tools to work with the SqlLite database for use with the
 * RoyalBlueRanger Mob Grinder.</p>
 *
 * <p>Table Relationships:</p>
 * <ul>
 *   <li>rbr_mob_grinder :: uuid pk
 *
 *     <ul>
 *       <li>rbr_mob_grinder_version - Version of the mob grinder tables
 *         <ul>
 *           <li>version: int - a simple int to track versions.</li>
 *         </ul>
 *       </li>
 *       <li>rbr_mob_grinder - Stats for each mob type.
 *         <ul>
 *           <li>pk: uuid</li>
 *         </ul>
 *       </li>
 *       <li>rbr_mob_grinder_event
 *         <ul>
 *           <li>pk: uuid, date</li>
 *           <li>Sparse table.</li>
 *           <li>Keep track of major events?</li>
 *           <li>Not 100% sure what this may be. Example may be if spawner location changes. How many times do they regenerate it?</li>
 *           <li>Fields: uuid, date, eventType, chunk coords, other_uuid, etc</li>
 *         </ul>
 *       </li>
 *       <li>rbr_mob_grinder_mobs
 *         <ul>
 *           <li>pk: uuid, mob_type</li>
 *           <li>Track stats on each mob type</li>
 *           <li>Spawned count, kill count, total cost, etc...</li>
 *         </ul>
 *       </li>
 *       <li>rbr_mob_grinder_materials
 *         <ul>
 *           <li>pk: uuid, material_type</li>
 *           <li>Track stats on each material type spent on grinding</li>
 *           <li>count, value</li>
 *         </ul>
 *       </li>
 *     </ul>
 *
 *   </li>
 *
 * </ul>
 *
 * @author RBR
 *
 */
public class RBRSqlLiteMaint
{


}
