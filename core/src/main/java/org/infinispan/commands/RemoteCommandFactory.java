package org.infinispan.commands;

import org.infinispan.CacheException;
import org.infinispan.commands.control.GetConsistentHashCommand;
import org.infinispan.commands.control.InstallConsistentHashCommand;
import org.infinispan.commands.control.JoinCompleteCommand;
import org.infinispan.commands.control.LockControlCommand;
import org.infinispan.commands.control.PullStateCommand;
import org.infinispan.commands.control.PushStateCommand;
import org.infinispan.commands.control.StateTransferControlCommand;
import org.infinispan.commands.read.GetKeyValueCommand;
import org.infinispan.commands.remote.ClusteredGetCommand;
import org.infinispan.commands.remote.MultipleRpcCommand;
import org.infinispan.commands.remote.SingleRpcCommand;
import org.infinispan.commands.tx.CommitCommand;
import org.infinispan.commands.tx.PrepareCommand;
import org.infinispan.commands.tx.RollbackCommand;
import org.infinispan.commands.write.ClearCommand;
import org.infinispan.commands.write.InvalidateCommand;
import org.infinispan.commands.write.InvalidateL1Command;
import org.infinispan.commands.write.PutKeyValueCommand;
import org.infinispan.commands.write.PutMapCommand;
import org.infinispan.commands.write.RemoveCommand;
import org.infinispan.commands.write.ReplaceCommand;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.factories.scopes.Scope;
import org.infinispan.factories.scopes.Scopes;
import org.infinispan.remoting.transport.Transport;

/**
 * Specifically used to create un-initialized {@link org.infinispan.commands.ReplicableCommand}s from a byte stream.
 *
 * @author Manik Surtani
 * @since 4.0
 */
@Scope(Scopes.GLOBAL)
public class RemoteCommandFactory {
   Transport transport;

   @Inject
   public void inject(Transport transport) {
      this.transport = transport;
   }

   /**
    * Creates an un-initialized command.  Un-initialized in the sense that parameters will be set, but any components
    * specific to the cache in question will not be set.
    * <p/>
    * You would typically set these parameters using {@link org.infinispan.commands.CommandsFactory#initializeReplicableCommand(ReplicableCommand)}
    * <p/>
    *
    * @param id         id of the command
    * @param parameters parameters to set
    * @return a replicable command
    */
   public ReplicableCommand fromStream(byte id, Object[] parameters) {
      ReplicableCommand command;
      switch (id) {
         case PutKeyValueCommand.COMMAND_ID:
            command = new PutKeyValueCommand();
            break;
         case LockControlCommand.COMMAND_ID:
            command = new LockControlCommand();
            break;
         case PutMapCommand.COMMAND_ID:
            command = new PutMapCommand();
            break;
         case RemoveCommand.COMMAND_ID:
            command = new RemoveCommand();
            break;
         case ReplaceCommand.COMMAND_ID:
            command = new ReplaceCommand();
            break;
         case GetKeyValueCommand.COMMAND_ID:
            command = new GetKeyValueCommand();
            break;
         case ClearCommand.COMMAND_ID:
            command = new ClearCommand();
            break;
         case PrepareCommand.COMMAND_ID:
            command = new PrepareCommand();
            break;
         case CommitCommand.COMMAND_ID:
            command = new CommitCommand();
            break;
         case RollbackCommand.COMMAND_ID:
            command = new RollbackCommand();
            break;
         case MultipleRpcCommand.COMMAND_ID:
            command = new MultipleRpcCommand();
            break;
         case SingleRpcCommand.COMMAND_ID:
            command = new SingleRpcCommand();
            break;
         case InvalidateCommand.COMMAND_ID:
            command = new InvalidateCommand();
            break;
         case InvalidateL1Command.COMMAND_ID:
            command = new InvalidateL1Command();
            break;
         case StateTransferControlCommand.COMMAND_ID:
            command = new StateTransferControlCommand();
            ((StateTransferControlCommand) command).init(transport);
            break;
         case ClusteredGetCommand.COMMAND_ID:
            command = new ClusteredGetCommand();
            break;
         case GetConsistentHashCommand.COMMAND_ID:
            command = new GetConsistentHashCommand();
            break;
         case InstallConsistentHashCommand.COMMAND_ID:
            command = new InstallConsistentHashCommand();
            break;
         case PushStateCommand.COMMAND_ID:
            command = new PushStateCommand();
            break;
         case PullStateCommand.COMMAND_ID:
            command = new PullStateCommand(transport);
            break;
         case JoinCompleteCommand.COMMAND_ID:
            command = new JoinCompleteCommand();
            break;
         default:
            throw new CacheException("Unknown command id " + id + "!");
      }
      command.setParameters(id, parameters);
      return command;
   }
}
